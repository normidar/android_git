package com.example.android_git;

import androidx.annotation.NonNull;


import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** AndroidGitPlugin */
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

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("getPlatformVersion")){
      // clone
      String remoteUrl = result;
      File localPath = result;
      if(!localPath.delete()) {
          throw new IOException("Could not delete temporary file " + localPath);
      }

      try (Git gitResult = Git.cloneRepository()
      .setURI(remoteUrl)
      .setDirectory(localPath)
      .setProgressMonitor(new SimpleProgressMonitor())
      .call()) {
        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
        System.out.println("Having repository: " + result.getRepository().getDirectory());
      }
    } else if(call.method.equals("getPlatformVersion")){
      // add 
      try (Repository repository = CookbookHelper.createNewRepository()) {
        try (Git git = new Git(repository)) {
            // create the file
            File myFile = new File(repository.getDirectory().getParent(), "testfile");
            if(!myFile.createNewFile()) {
                throw new IOException("Could not create file " + myFile);
            }

            // run the add-call
            git.add()
                    .addFilepattern("testfile")
                    .call();

            System.out.println("Added file " + myFile + " to repository at " + repository.getDirectory());
        }
      }

    } else if(call.method.equals("getPlatformVersion")){
      // commit
      try (Git git = new Git(repository)) {
        // create the file
        File myFile = new File(repository.getDirectory().getParent(), "testfile");
        if(!myFile.createNewFile()) {
            throw new IOException("Could not create file " + myFile);
        }

        // Stage all files in the repo including new files, excluding deleted files
        // git.add().addFilepattern(".").call();

        // Stage all changed files, including deleted files, excluding new files
        // git.add().addFilepattern(".").setUpdate(true).call();

        // and then commit the changes.
        git.commit()
                .setMessage("Commit all changes including additions")
                .call();

        try(PrintWriter writer = new PrintWriter(myFile)) {
            writer.append("Hello, world!");
        }

        // Stage all changed files, omitting new files, and commit with one command
        git.commit()
                .setAll(true)
                .setMessage("Commit changes to all files")
                .call();


        System.out.println("Committed all changes to repository at " + repository.getDirectory());
      }
    // } else if(call.method.equals("getPlatformVersion")){
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
