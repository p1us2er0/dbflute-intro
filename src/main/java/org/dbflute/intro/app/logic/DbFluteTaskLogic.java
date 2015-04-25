package org.dbflute.intro.app.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dbflute.optional.OptionalThing;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteTaskLogic {

    public boolean execute(String project, String task, OptionalThing<String> env, OutputStream outputStream) {
        List<ProcessBuilder> dbfluteTaskList;
        if ("doc".equals(task)) {
            dbfluteTaskList = getDocCommandList();
        } else if ("loadDataReverse".equals(task)) {
            dbfluteTaskList = getLoadDataReverseCommandList();
        } else if ("schemaSyncCheck".equals(task)) {
            dbfluteTaskList = getSchemaSyncCheckCommandList();
        } else if ("replaceSchema".equals(task)) {
            dbfluteTaskList = getReplaceSchemaCommandList();
        } else {
            throw new RuntimeException("タスク不正");
        }

        boolean result = dbfluteTaskList.stream().allMatch(processBuilder -> {
            processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

            Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
            env.ifPresent(value -> {
                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + value);
            });
            processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

            int resultCode = executeCommand(processBuilder, outputStream);
            return resultCode == 0;
        });

        return result;
    }

    public List<ProcessBuilder> getDocCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "doc"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getLoadDataReverseCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "load-data-reverse"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getSchemaSyncCheckCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "schema-sync-check"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getReplaceSchemaCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "replace-schema"));

        return getCommandList(commandList);
    }

    protected List<ProcessBuilder> getCommandList(List<List<String>> commandList) {

        List<ProcessBuilder> processBuilderList = new ArrayList<ProcessBuilder>();
        for (List<String> command : commandList) {

            if (command.isEmpty()) {
                continue;
            }

            String onName = System.getProperty("os.name").toLowerCase();
            List<String> list = new ArrayList<String>();
            if (onName.startsWith("windows")) {
                list.add("cmd");
                list.add("/c");
                list.add(command.get(0) + ".bat");
            } else {
                list.add("sh");
                list.add(command.get(0) + ".sh");
            }

            if (command.size() > 1) {
                list.addAll(command.subList(1, command.size()));
            }

            processBuilderList.add(new ProcessBuilder(list));
        }

        return processBuilderList;
    }

    public int executeCommand(ProcessBuilder processBuilder, OutputStream outputStream) {

        processBuilder.redirectErrorStream(true);

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        Process process;

        int result = 0;
        try {
            process = processBuilder.start();
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                IOUtils.write(line, outputStream);
                IOUtils.write(System.getProperty("line.separator"), outputStream);
                outputStream.flush();

                if (line.equals("BUILD FAILED")) {
                    result = 1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(inputStream);
        }

        if (result == 0) {
            result = process.exitValue();
        }

        return result;
    }
}
