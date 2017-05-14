package com.brc.action;

import com.brc.action.flowparse.*;
import com.brc.action.parse.*;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

public class FlowFileGenerateAction {
    public static void engineEntry(TableModel tableModel)
            throws Exception {
        String basePath = StringUtil.replaceSlash(tableModel.getBasePath());
        tableModel.setBasePath(basePath);
        if (tableModel.isBeanBol()) {
            new BeanParser().generate(tableModel);
        }
        if (tableModel.isXMLBol()) {
            new FlowXMLParser().generate(tableModel);
        }
        if (tableModel.isServiceBol()) {
            new FlowServiceParser().generate(tableModel);
        }
        if (tableModel.isActionBol()) {
            new FlowActionParser().generate(tableModel);
        }
        if (tableModel.isJspBol()) {
            new FlowJSPParser().generate(tableModel);
        }
        new FlowBpmParser().generate(tableModel);

    }
}

