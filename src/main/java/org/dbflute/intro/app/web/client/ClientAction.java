package org.dbflute.intro.app.web.client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.logic.DbFluteIntroLogic;
import org.dbflute.intro.app.logic.DbFluteTaskLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.ActionResponse;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.lastaflute.web.response.StreamResponse;
import org.dbflute.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author p1us2er0
 */
public class ClientAction extends DbfluteIntroBaseAction {

    @Resource
    private DbFluteClientLogic dbFluteClientLogic;

    @Resource
    private DbFluteTaskLogic dbFluteTaskLogic;

    /** The manager of response. (NotNull) */
    @Resource
    private ResponseManager responseManager;

    @Execute
    public JsonResponse list() {
        List<String> projectList = dbFluteClientLogic.getProjectList();
        return asJson(projectList);
    }

    @Execute
    public JsonResponse detail(String project) {
        ClientBean clientBean = dbFluteClientLogic.convClientBeanFromDfprop(project);
        return asJson(clientBean);
    }

    @Execute
    public JsonResponse add(ClientForm clientForm) {
        ClientBean clientBean = clientForm.clientBean;
        Map<String, DatabaseBean> schemaSyncCheckMap = clientForm.schemaSyncCheckMap;
        dbFluteClientLogic.createNewClient(clientBean, schemaSyncCheckMap);
        return asJson(clientBean);
    }

    @Execute
    public JsonResponse remove(String project) {
        dbFluteClientLogic.deleteClient(project);
        return asJson(true);
    }

    @Execute
    public JsonResponse update(ClientForm clientForm) {
        ClientBean clientBean = clientForm.clientBean;
        Map<String, DatabaseBean> schemaSyncCheckMap = clientForm.schemaSyncCheckMap;
        dbFluteClientLogic.createNewClient(clientBean, schemaSyncCheckMap);
        return asJson(clientBean);
    }

    @Execute
    public ActionResponse task(String project, String task) {

        HttpServletResponse response = responseManager.getResponse();
        response.setContentType("text/plain; charset=UTF-8");
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ProcessBuilder> taskList = dbFluteTaskLogic.getJdbcDocCommandList();
        for (final ProcessBuilder processBuilder : taskList) {

            processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

            Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
//            if (getValue(NAME).toString().equals(LABEL_SYNC_CHECK)) {
//                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + env);
//            }

            processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

            int result = dbFluteTaskLogic.executeCommand(processBuilder, outputStream);
        }

        return ActionResponse.empty();
    }

    @Execute
    public ActionResponse schemahtml(String project) {
        String filePath = "dbflute_" + project + "/output/doc/schema-" + project + ".html";
        return createHtmlStreamResponse(filePath);
    }

    @Execute
    public ActionResponse historyhtml(String project) {
        String filePath = "dbflute_" + project + "/output/doc/history-" + project + ".html";
        return createHtmlStreamResponse(filePath);
    }

    protected StreamResponse createHtmlStreamResponse(String filePath) {
        File file = new File(DbFluteIntroLogic.BASE_DIR_PATH, filePath);
        StreamResponse streamResponse = new StreamResponse("");
        streamResponse.contentType("text/html; charset=UTF-8");
        try {
            streamResponse.stream(FileUtils.openInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return streamResponse;
    }
}
