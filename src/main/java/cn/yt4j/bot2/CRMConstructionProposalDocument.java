package cn.yt4j.bot2;


import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class CRMConstructionProposalDocument {

    public static void main(String[] args) {
        // 创建一个新的Word文档
        XWPFDocument document = new XWPFDocument();

        try (FileOutputStream out = new FileOutputStream("CRM_Construction_Proposal_Document.docx")) {

            // 设置标题
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("洛阳市人才集团CRM建设方案");
            titleRun.setFontSize(22); // 二号字体
            titleRun.setFontFamily("方正小标宋简体");
            titleRun.setBold(true);

            // 添加公司信息
            XWPFParagraph companyInfo = document.createParagraph();
            companyInfo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun companyRun = companyInfo.createRun();
            companyRun.setText("洛阳市人才集团数智中心");
            companyRun.setFontSize(12);

            // 添加空行
            document.createParagraph().createRun().addBreak();

            // 添加主要部分
            addSection(document, "一、需求概述");
            addContent(document,
                    "为支撑集团“12356”战略，推动实现高质量可持续发展，以实现集团全面信息化、数字化为目标，" +
                            "围绕相关部门、中心和子公司的管理现状和业务需求，对系统建设进行整体规划，搭建集团统一的营销服业务支撑平台；\n" +
                            "围绕集团和下属单位双视角，优先实现集团总部主干统一、各业务板块/子公司末端灵活，沉淀标准销售方法论，强化监管，透视业务；\n" +
                            "目前运用ERP、OA等信息化系统，已具有一定的信息化基础，但缺乏统一工具作为全流程管理抓手，导致业务链割裂，" +
                            "造成组织之间、角色之间的信息共享度低、协作性不高、业务流程不畅、数据统计分析难等问题突出，" +
                            "因此当前阶段的CRM建设周期重点放在客户管理、跟进管理、合同管理、回款管理的建设，使内部信息化形成闭环，" +
                            "一定程度上保障业务规范性，降低合同履约风险。");
            // 添加厂商概况部分
            addSection(document, "二、厂商概况");

            // 添加纷享逍客信息
            addSubsection(document, "1. 纷享逍客");
            addContent(document,
                    "纷享逍客是行业领先的CRM厂商，拥有成熟的SOP流程。作为行业第一的品牌，虽然提供标准的销售流程和服务，" +
                            "但也意味着链路较长、流程较为繁琐。此类服务适合追求标准流程且预算充足的公司。");

            // 添加炎黄盈动信息
            addSubsection(document, "2. 炎黄盈动");
            addContent(document,
                    "炎黄盈动是一家提供OA系统的厂商，其CRM模块由无代码系统拼装而成。虽然功能不如纷享逍客强大，但能够支持公司按业务流程自行搭建功能，" +
                            "且不需要额外付费。适合OA需求强且希望CRM模块更灵活的公司，通过无代码方式添加模块可满足基本CRM需求。");

            addSection(document, "三、技术方案");
            addSubsection(document, "3.1 系统架构");
            addContent(document, "CRM系统分模块结构，以客户管理、合同管理、应收与回款、数据分析等为核心模块，通过系统集成与现有系统如OA、财务系统对接，实现数据共享。");

            addSubsection(document, "3.2 技术方案可行性分析");
            addContent(document, "分析自研或外购CRM系统：自研定制化强，外购系统开发周期短。建议采取外部商用系统并行方案。");

            addSubsection(document, "3.3 接口方案");
            addContent(document, "设计标准接口，与财务系统、OA系统的数据实时同步更新，支持信息共享和权限管理。");

            addSection(document, "四、实施方案");
            addSubsection(document, "4.1 实施流程");
            addContent(document, "实施流程将从需求分析开始，经过开发、测试、部署上线的完整流程，采用敏捷开发模式，确保项目进度可控。");

            addSubsection(document, "4.2 分阶段实施计划");
            addContent(document, "第一阶段：上线核心功能；第二阶段：扩展功能；第三阶段：多子公司个性化应用的支持和业务系统整合。");

            addSection(document, "五、盈利与价值分析");
            addContent(document, "CRM系统将带来管理效率的提升，增强客户信息的可视化，优化合同管理和回款管理流程，为管理层提供多维度的经营数据分析。");

            addSection(document, "六、风险及应对措施");
            addSubsection(document, "6.1 风险识别");
            addContent(document, "在实施过程中可能面临的技术、业务流程及数据安全风险，包括系统集成困难、业务流程适应性差等问题。");

            addSubsection(document, "6.2 应对策略");
            addContent(document, "设立风险评估机制、定期沟通、技术支持预案，保障项目进度和系统的稳定性，同时确保数据安全和用户隐私的保护。");

            // 保存文档
            document.write(out);
            System.out.println("CRM建设方案文档生成成功！");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 添加主标题
    private static void addSection(XWPFDocument document, String title) {
        XWPFParagraph section = document.createParagraph();
        section.setSpacingAfter(200);
        XWPFRun run = section.createRun();
        run.setText(title);
        run.setFontSize(16);  // 一级标题
        run.setBold(true);
    }

    // 添加副标题
    private static void addSubsection(XWPFDocument document, String title) {
        XWPFParagraph subsection = document.createParagraph();
        subsection.setSpacingAfter(150);
        XWPFRun run = subsection.createRun();
        run.setText(title);
        run.setFontSize(14);  // 二级标题
        run.setBold(true);
    }

    // 添加正文内容
    private static void addContent(XWPFDocument document, String content) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(80);
        XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontSize(12);
    }
}


