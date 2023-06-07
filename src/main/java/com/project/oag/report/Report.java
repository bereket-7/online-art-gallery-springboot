package com.project.oag.report;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "report")
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "report_detail", nullable = false)
	private String reportDetail;
	@Column(name = "report_name", nullable = false)
	private String reportTitle;
	@Column(name = "reporter_name")
	private String reporterName;
	@Column(name = "reporter_email")
	private String reporterEmail;
	public Report() {
		super();
	}
	public Report(String reportDetail, String reportTitle, String reporterName, String reporterEmail) {
		super();
		this.reportDetail = reportDetail;
		this.reportTitle = reportTitle;
		this.reporterName = reporterName;
		this.reporterEmail = reporterEmail;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getReporterEmail() {
		return reporterEmail;
	}

	public void setReporterEmail(String reporterEmail) {
		this.reporterEmail = reporterEmail;
	}
}
