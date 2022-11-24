import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'android_git_method_channel.dart';

abstract class AndroidGitPlatform extends PlatformInterface {
  /// Constructs a AndroidGitPlatform.
  AndroidGitPlatform() : super(token: _token);

  static final Object _token = Object();

  static AndroidGitPlatform _instance = MethodChannelAndroidGit();

  /// The default instance of [AndroidGitPlatform] to use.
  ///
  /// Defaults to [MethodChannelAndroidGit].
  static AndroidGitPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AndroidGitPlatform] when
  /// they register themselves.
  static set instance(AndroidGitPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
