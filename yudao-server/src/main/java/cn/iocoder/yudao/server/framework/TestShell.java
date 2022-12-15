package cn.iocoder.yudao.server.framework;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TestShell {
    public static void main(String[] args) throws IOException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("flowable-task-Executor-");
        executor.setAwaitTerminationSeconds(5);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        executor.execute((() -> {
            try {
                testShell("EDSSS");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        executor.execute((() -> {
            try {
                testShell("EDSSS123");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        System.out.println("end++++++++++++++++");
    }
//    ansible-playbook -i hosts-dev  task-entry-of-role-init-env.yml -t config-hosts
    private static void testShell(String taskId) throws IOException {
        ProcessBuilder process = new ProcessBuilder();
        Process ansiblePlayProcess = process.directory(new File("/Users/zero/Workspace/code/udesk/udesk_local_deploy"))
                .command(
                        "/usr/local/bin/ansible-playbook",
                        "-i", "hosts-dev",
                        "task-entry-of-role-init-env.yml",
                        "-t", "config-hosts"
                )
                .start();
        ArrayList<String> result = new ArrayList<>();
        InputStream in = null;
        try {
            in = ansiblePlayProcess.getInputStream();
            BufferedReader reader = IoUtil.getReader(in, Charset.defaultCharset());
            IoUtil.readLines(reader, (LineHandler) System.out::println);
            IoUtil.readLines(in, Charset.defaultCharset(), result);
        } finally {
            IoUtil.close(in);
            ansiblePlayProcess.destroy();
        }
    }
}
