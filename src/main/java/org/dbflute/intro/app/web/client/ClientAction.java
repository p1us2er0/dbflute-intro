package org.dbflute.intro.app.web.client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.logic.DbFluteIntroLogic;
import org.dbflute.intro.app.logic.DbFluteTaskLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.lastaflute.web.response.StreamResponse;
import org.dbflute.lastaflute.web.servlet.request.ResponseManager;
import org.dbflute.optional.OptionalThing;

/**
 * @author p1us2er0
 */
public class ClientAction extends DbfluteIntroBaseAction {

    @Resource
    private DbFluteClientLogic dbFluteClientLogic;

    @Resource
    private DbFluteTaskLogic dbFluteTaskLogic;

    @Resource
    private ResponseManager responseManager;

    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classification() {
        Map<String, Map<?, ?>> classificationMap = dbFluteClientLogic.getClassificationMap();
        return asJson(classificationMap);
    }

    @Execute
    public JsonResponse<List<ClientResponseBean>> list() {
        List<String> projectList = dbFluteClientLogic.getProjectList();
        List<ClientResponseBean> clientResponseBeanList = projectList.stream().map(project -> {
            ClientBean clientBean = dbFluteClientLogic.convClientBeanFromDfprop(project);
            return convertClientResponseBean(clientBean);
        }).collect(Collectors.toList());

        return asJson(clientResponseBeanList);
    }

    @Execute
    public JsonResponse<ClientResponseBean> detail(String project) {
        ClientBean clientBean = dbFluteClientLogic.convClientBeanFromDfprop(project);
        ClientResponseBean clientResponseBean = convertClientResponseBean(clientBean);
        return asJson(clientResponseBean);
    }

    protected ClientResponseBean convertClientResponseBean(ClientBean clientBean) {
        ClientResponseBean clientResponseBean = new ClientResponseBean();
        clientResponseBean.setClientBean(clientBean);
        String project = clientBean.getProject();
        clientResponseBean.setSchemahtml(calcFile(project, "schema").exists());
        clientResponseBean.setHistoryhtml(calcFile(project, "history").exists());
        clientResponseBean.setReplaceSchema(dbFluteClientLogic.existReplaceSchemaFile(project));
        return clientResponseBean;
    }

    @Execute
    public JsonResponse<Void> create(ClientForm clientForm) {
        validate(clientForm, () -> dispatchApiValidationError());
        ClientBean clientBean = clientForm.clientBean;
        if (clientForm.testConnection) {
            dbFluteClientLogic.testConnection(clientBean);
        }
        dbFluteClientLogic.createClient(clientBean, clientForm.testConnection);
        return JsonResponse.empty();
    }

    @Execute
    public JsonResponse<Void> update(ClientForm clientForm) {
        validate(clientForm, () -> dispatchApiValidationError());
        ClientBean clientBean = clientForm.clientBean;
        if (clientForm.testConnection) {
            dbFluteClientLogic.testConnection(clientBean);
        }
        dbFluteClientLogic.updateClient(clientBean, clientForm.testConnection);
        return JsonResponse.empty();
    }

    @Execute
    public JsonResponse<Void> delete(String project) {
        dbFluteClientLogic.deleteClient(project);
        return JsonResponse.empty();
    }

    @Execute
    public JsonResponse<Void> task(String project, String task, OptionalThing<String> env) {
        HttpServletResponse response = responseManager.getResponse();
        response.setContentType("text/plain; charset=UTF-8");
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dbFluteTaskLogic.execute(project, task, env, outputStream);
        return JsonResponse.skip();
    }

    @Execute
    public StreamResponse schemahtml(String project) {
        return createHtmlStreamResponse(calcFile(project, "schema"));
    }

    @Execute
    public StreamResponse historyhtml(String project) {
        return createHtmlStreamResponse(calcFile(project, "history"));
    }

    protected File calcFile(String project, String type) {
        File file = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/output/doc/" + type + "-" + project + ".html");
        return file;
    }

    protected StreamResponse createHtmlStreamResponse(File file) {

        StreamResponse streamResponse = new StreamResponse("");
        streamResponse.contentType("text/html; charset=UTF-8");
        try {
            streamResponse.stream(FileUtils.openInputStream(file));
        } catch (IOException e) {
            streamResponse.httpStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return streamResponse;
    }
}
