package upc.c505.modular.villageiot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.maintain.entity.CascadeConfig;
import upc.c505.modular.maintain.mapper.CascadeConfigMapper;
import upc.c505.modular.maintain.util.pdf.PdfUtil;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.controller.param.pdfparam.ExportPdfParam;
import upc.c505.modular.villageiot.controller.param.pdfparam.PicTurePdf;
import upc.c505.modular.villageiot.entity.VideoInspectionRecord;
import upc.c505.modular.villageiot.mapper.VideoInspectionRecordMapper;
import upc.c505.modular.villageiot.service.IVideoInspectionRecordService;
import upc.c505.modular.villageiot.service.IVideoRandomInspectionService;
import upc.c505.modular.villageiot.util.VideoUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2024-06-28
 */
@Service
@Slf4j
public class VideoInspectionRecordServiceImpl extends ServiceImpl<VideoInspectionRecordMapper, VideoInspectionRecord> implements IVideoInspectionRecordService {

    private final String DATA_MAP = "dateMap";
    private final String IMAGE_MAP = "imageMap";
    @Autowired
    private VideoInspectionRecordMapper videoInspectionRecordMapper;
    @Autowired
    private ISysAreaService sysAreaService;
    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private CascadeConfigMapper cascadeConfigMapper;

    @Autowired
    private IVideoRandomInspectionService videoRandomInspectionService;

    @Override
    public Long sendWarning(VideoInspectionRecord videoInspectionRecord) {
        //查询出用户记录并更改违规次数
        List<VideoInspectionRecord> records = videoInspectionRecordMapper.selectList(
                new MyLambdaQueryWrapper<VideoInspectionRecord>()
                        .eq(VideoInspectionRecord::getEntCreditCode, videoInspectionRecord.getEntCreditCode())
        );
        VideoInspectionRecord record = new VideoInspectionRecord();
        BeanUtils.copyProperties(videoInspectionRecord, record);
        record.setInspectionTime(LocalDateTime.now())
                .setYearlyViolationFrequency(records.size() + 1);
        videoInspectionRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public Page<VideoInspectionRecord> selectInspectionRecordPage(InspectionRecordPageSearchParam param) {
        MyLambdaQueryWrapper<VideoInspectionRecord> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        /*
         * 如果查询的是管辖区域
         *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
         *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
         *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
         *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
         *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
            //获取当前用户的管辖区域列表
            List<Long> list = peopleGovernmentService.getManageAreaIdList();
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!list.contains(param.getAreaId())) {
                    return new Page<>();
                }
                //当传入的areaId合法时
                // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoInspectionRecord::getAreaId, param.getAreaId());
                }
                //当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    list.retainAll(areaIdList);
                    lambdaQueryWrapper.in(VideoInspectionRecord::getAreaId, list);
                }
            } else {
                lambdaQueryWrapper.in(VideoInspectionRecord::getAreaId, list);
            }
        }
        /*
         * 如果需要查适用区域：
         * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户所在区域及子区域内，
         * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
         * 2、如果传入的areaId为空，那就查该用户areaId的数据。
         */
        if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 1) {
            List<Long> areaIdList = sysAreaService.getChildAreaIdList(UserUtils.get().getAreaId());
            if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                //如果传入的areaId不合法，直接返回空的页面
                if (!areaIdList.contains(param.getAreaId())) {
                    return new Page<>();
                }
                //如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，当flag为1时查询传入areaId及其子区域的数据。
                if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                    lambdaQueryWrapper.eq(VideoInspectionRecord::getAreaId, param.getAreaId());
                }
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                    lambdaQueryWrapper.in(VideoInspectionRecord::getAreaId, areaIdList);
                }
            } else {
                lambdaQueryWrapper.eq(VideoInspectionRecord::getAreaId, UserUtils.get().getAreaId());
            }
        }
        //如果是建设用户或者企业用户，那么number不为空，去判断一下权限
        List<String> numberList = videoRandomInspectionService.judgeAuthority();
        if (numberList.size() != 0) {
            lambdaQueryWrapper.in(VideoInspectionRecord::getEntCreditCode, numberList);
        }

        //2024.7.23新增查询条件：详细匹配参数：巡查处理人、是否答复、巡查日期区间
        lambdaQueryWrapper
                .eq(ObjectUtils.isNotEmpty(param.getHandle()), VideoInspectionRecord::getHandle, param.getHandle())
                .eq(ObjectUtils.isNotEmpty(param.getIsReply()), VideoInspectionRecord::getIsReply, param.getIsReply())
                .between(ObjectUtils.isNotEmpty(param.getStartInspectionTime()) && ObjectUtils.isNotEmpty(param.getEndInspectionTime()), VideoInspectionRecord::getInspectionTime,
                        param.getStartInspectionTime(), param.getEndInspectionTime())
                .eq(ObjectUtils.isNotEmpty(param.getEntCreditCode()),
                        VideoInspectionRecord::getEntCreditCode, param.getEntCreditCode())
                .eq(ObjectUtils.isNotEmpty(param.getMonitorAttributes()),
                        VideoInspectionRecord::getMonitorAttributes, param.getMonitorAttributes())
                .like(ObjectUtils.isNotEmpty(param.getEnterpriseName()),
                        VideoInspectionRecord::getEnterpriseName, param.getEnterpriseName())
                .orderByDesc(VideoInspectionRecord::getAddDatetime);
        Page<VideoInspectionRecord> p = new Page<>(param.getCurrent(), param.getSize());
        return videoInspectionRecordMapper.selectPage(p, lambdaQueryWrapper);
    }

    @Override
    public List<InspectionAnalysisReturnParam> inspectionAnalysis(InspectionAnalysisSearchParam param) {
        List<InspectionAnalysisReturnParam> paramList = new ArrayList<>();
        //将级联表中存放违规信息的数据（type = 2）的数去取出，以id为key映射成map
        List<CascadeConfig> cascadeConfigList = cascadeConfigMapper.selectList(
                new MyLambdaQueryWrapper<CascadeConfig>()
                        .eq(CascadeConfig::getType, "2")
        );
        Map<Integer, CascadeConfig> cascadeConfigMap = cascadeConfigList
                .stream()
                .collect(Collectors.toMap(CascadeConfig::getId, (item) -> item));
        //查出信息，查询对应areaId下的数据，并按照许可证号分组
        Map<String, List<VideoInspectionRecord>> recordMap = videoInspectionRecordMapper.selectList(new MyLambdaQueryWrapper<VideoInspectionRecord>()
                        .eq(ObjectUtils.isNotEmpty(param.getEntCreditCode()), VideoInspectionRecord::getCoaCode, param.getEntCreditCode())
                        .eq(VideoInspectionRecord::getAreaId, param.getAreaId())
                        .between(VideoInspectionRecord::getHandleTime,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-01-01 00:00:00")),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-12-31 23:59:59"))))
                .stream().collect(Collectors.groupingBy(VideoInspectionRecord::getCoaCode));
        //遍历分组后的recordMap，将信息进行处理
        if (ObjectUtils.isNotEmpty(recordMap)) {
            recordMap.forEach((coaCode, list) -> {
                //创建一个set去除重复信息
                Set<String> messageSet = new HashSet<>();
                log.info(list.toString());
                InspectionAnalysisReturnParam param1 = new InspectionAnalysisReturnParam();
                param1.setCoaCode(coaCode);
                //用来存放违规信息
                StringJoiner message = new StringJoiner(",", "存在", "等违规行为");
                System.out.println("the size of list is" + list.size());
                list.forEach(record -> {
                    //解析record,设置message
                    List<List> lists = JSON.parseArray(record.getRecord(), List.class);
                    System.out.println("the lists size is: " + lists.size());
                    if (CollectionUtils.isEmpty(lists)) {
                        return;
                    }
                    //将所有信息插入到set中，大类转换成小类
                    if (lists.size() != 0) {
                        Map<String, Set<String>> map = VideoUtil.unfoldList(lists);
                        messageSet.addAll(map.get(VideoUtil.SUB_CLASS));
                        map.get(VideoUtil.PARENT_CLASS).forEach(
                                item -> {
                                    cascadeConfigList.forEach(cascadeConfig -> {
                                        if (cascadeConfig.getParentId().toString().equals(item)) {
                                            messageSet.add(String.valueOf(cascadeConfig.getId()));
                                        }
                                    });
                                }
                        );
                        //将对应的值值加到param属性中
                        messageSet.forEach(item -> setProperties(param1, Integer.valueOf(item)));
                    }
                });
                //设置message
                messageSet.forEach(str -> message.add(cascadeConfigMap.get(Integer.valueOf(str)).getName()));
                param1.setMessage(message.toString());
                paramList.add(param1);
            });
        }
        return paramList;
    }

    @Override
    public void generateInspectionBill(Long id, HttpServletResponse response) {
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String filePath = "upload/template/重点单位视频远程执法巡查单模板.pdf";
//        String filePath = "D:/future_village/future_village2/future_village2/main/src/main/resources/templates/重点单位视频远程执法巡查单模板.pdf";
        Map<String, Object> map = new HashMap<>();
        VideoInspectionRecord record = this.getById(id);
        if (Objects.equals(record.getMonitorAttributes(), VideoConst.PROJECT)) {
            filePath = "upload/template/重点项目视频远程执法巡查单模板.pdf";
        } else if (Objects.equals(record.getMonitorAttributes(), VideoConst.PARTY)) {
            filePath = "upload/template/党组织视频远程执法巡查单模板.pdf";
        }
        //这里面放和文字有关的参数
        ExportPdfParam exportPdfParam = new ExportPdfParam();
        BeanUtils.copyProperties(record, exportPdfParam);
        if (ObjectUtils.isNotEmpty(record.getHandleTime())) {
            exportPdfParam.setHandleTime(dfDate.format(record.getHandleTime()));
        }
        if (ObjectUtils.isNotEmpty(record.getInspectionTime())) {
            exportPdfParam.setInspectionTime(dfDate.format(record.getInspectionTime()));
        }
        if (ObjectUtils.isNotEmpty(record.getRecord())) {
            exportPdfParam.setRecord(idToMessage(record.getRecord()));
        }
        //图片路径
        //这里存图片
        PicTurePdf picTurePdf = new PicTurePdf();
        List<String> pictureUrls = JSON.parseArray(record.getPicture(), PictureParam.class)
                .stream()
                .map(PictureParam::getUrl)
                .collect(Collectors.toList());
        int size = pictureUrls.size();
        picTurePdf
                .setPicture1(size >= 1 ? pictureUrls.get(0) : null)
                .setPicture2(size >= 2 ? pictureUrls.get(1) : null)
                .setPicture3(size >= 3 ? pictureUrls.get(2) : null)
                .setPicture4(size >= 4 ? pictureUrls.get(3) : null);
        Map exportPdfParamMap = JSON.parseObject(JSON.toJSONString(exportPdfParam), Map.class);
        Map picMap = JSON.parseObject(JSON.toJSONString(picTurePdf), Map.class);
        exportPdfParamMap.remove("picTurePdf");
        map.put(DATA_MAP, exportPdfParamMap);
        map.put(IMAGE_MAP, picMap);
        try {
            //----------------------------------------------------------------------4.修改导出名称-------------------------------------------------------------------------------
            String fileNameStr = URLEncoder.encode("视频远程执法巡查单", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileNameStr + ".pdf");
            OutputStream out = response.getOutputStream();
            PdfUtil.exportPDF(map, filePath, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据库中存储的record值通过查询字典表映射成记录
     *
     * @return 记录值
     */
    private String idToMessage(String idListString) {
        //解析record,设置message
        List<List> lists = JSON.parseArray(idListString, List.class);
        StringJoiner message = new StringJoiner(",", "存在", "等问题");
        //将级联表中存放违规信息的数据（type = 2）的数去取出，以id为key映射成map
        List<CascadeConfig> cascadeConfigList = cascadeConfigMapper.selectList(
                new MyLambdaQueryWrapper<CascadeConfig>()
                        .eq(CascadeConfig::getType, "2")
        );
        Map<Integer, CascadeConfig> cascadeConfigMap = cascadeConfigList
                .stream()
                .collect(Collectors.toMap(CascadeConfig::getId, (item) -> item));
        //        将集合展开并去重
        Map<String, Set<String>> map = VideoUtil.unfoldList(lists);
        Set<String> stringHashSet = new HashSet<>(map.get(VideoUtil.SUB_CLASS));
        map.get(VideoUtil.PARENT_CLASS).forEach(
                item -> {
                    cascadeConfigList.forEach(
                            cascadeConfig -> {
                                if (cascadeConfig.getParentId().toString().equals(item)) {
                                    stringHashSet.add(cascadeConfig.getId().toString());
                                }
                            }
                    );
                }
        );
        stringHashSet.forEach(item -> message.add(cascadeConfigMap.get(Integer.valueOf(item)).getName()));
        return message.toString();
    }

    @Override
    public Map<String, Integer> countRemoteSupervision(List<String> projectNumberList) {
        return videoInspectionRecordMapper.countRemoteSupervision(VideoConst.PROJECT, projectNumberList).stream()
                .collect(Collectors.toMap(CountRemoteSupervisionReturnParam::getProjectNumber, CountRemoteSupervisionReturnParam::getRemoteSupervisionNumber));
    }

    /**
     * 给对象设置相应的属性值
     *
     * @param param 要设置值的参数
     * @param k     匹配项
     */
    private void setProperties(InspectionAnalysisReturnParam param, Integer k) {
        switch (k) {
            case 294:
                param.setSmoking(param.getSmoking() + 1);
                break;
            case 301:
                param.setBug(param.getBug() + 1);
                break;
            case 296:
                param.setMouseAppearing(param.getMouseAppearing() + 1);
                break;
            case 297:
                param.setNoMask(param.getNoMask() + 1);
                break;
            case 300:
                param.setNoHat(param.getNoHat() + 1);
                break;
            case 302:
                param.setUntidyGround(param.getUntidyGround() + 1);
                break;
            default:
                break;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class PictureParam {
        private String name;
        private String url;
        private String uid;
        private String status;

    }
}
