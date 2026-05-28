package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.dto.request.ReportRequest;
import com.campus.marketplace.entity.Report;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;

    public void createReport(Long reporterId, ReportRequest request) {
        // 防重复举报：同一举报者对同一目标且未处理时拒绝
        LambdaQueryWrapper<Report> w = new LambdaQueryWrapper<>();
        w.eq(Report::getReporterId, reporterId)
                .eq(Report::getTargetType, request.getTargetType())
                .eq(Report::getTargetId, request.getTargetId())
                .eq(Report::getStatus, 0);
        if (reportMapper.selectCount(w) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "已举报过该商品，请等待处理");
        }

        Report report = new Report();
        report.setReporterId(reporterId);
        report.setTargetType(request.getTargetType());
        report.setTargetId(request.getTargetId());
        report.setType(request.getType());
        report.setReason(request.getReason());
        report.setStatus(0);
        reportMapper.insert(report);
    }
}
