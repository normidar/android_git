package com.example.android_git;

import androidx.annotation.NonNull;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.EmptyProgressMonitor;
import org.eclipse.jgit.lib.Repository;

import java.io.File;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * AndroidGitPlugin
 */
public class AndroidGitPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "android_git");
        channel.setMethodCallHandler(this);
    }

    private Repository openRepository(String path) throws Exception {
        Git git = Git.open(new File(path));
        return git.getRepository();
    }

    private Git openGit(String path) throws Exception {
        return Git.open(new File(path));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("clone")) {
            // clone
            String remoteUrl = call.argument("remoteUrl");
            String localPath = call.argument("localPath");
            File dir = new File(localPath);

            try {
                Git gitResult = Git.cloneRepository()
                        .setURI(remoteUrl)
                        .setDirectory(dir)
                        .setProgressMonitor(new EmptyProgressMonitor() {
                            @Override
                            public void endTask() {
                                super.endTask();
                                result.success(null);
                            }
                        })
                        .call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } else if (call.method.equals("add")) {
            String repositoryPath = call.argument("repositoryPath");
            String filePath = call.argument("filePath");
            Repository repository = null;
            try {
                repository = openRepository(repositoryPath);

                Git git = new Git(repository);
                // run the add-call
                git.add()
                        .addFilepattern(filePath)
                        .call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call.method.equals("addAll")) {
            String repositoryPath = call.argument("repositoryPath");
            String filePath = call.argument("filePath");
            try {
                Repository repository = openRepository(repositoryPath);
                Git git = new Git(repository);
                // Stage all files in the repo including new files, excluding deleted files
                git.add().addFilepattern(".").call();

                // Stage all changed files, including deleted files, excluding new files
                git.add().addFilepattern(".").setUpdate(true).call();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call.method.equals("commit")) {
            // commit
            String repositoryPath = call.argument("repositoryPath");
            String message = call.argument("message");

            try {
                Git git = new Git(openRepository(repositoryPath));
                // commit the changes.
                git.commit()
                        .setMessage("Commit all changes including additions")
                        .call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call.method.equals("push")) {
            // push
            String repositoryPath = call.argument("repositoryPath");
            String remoteName = call.argument("remoteName");
            String keyPath = call.argument("keyPath");

            Git git = null;
            try {
                git = openGit(repositoryPath);
            } catch (Exception e) {
                e.printStackTrace();
            }


            PushCommand pushCommand = git.push();
            pushCommand.setRemote(remoteName);
            pushCommand.setProgressMonitor(new EmptyProgressMonitor() {
                @Override
                public void endTask() {
                    super.endTask();
                }
            });
            pushCommand.setTransportConfigCallback(new SshTransportConfigCallback(keyPath));
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
            // } else if(call.method.equals("getPlatformVersion")){
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
