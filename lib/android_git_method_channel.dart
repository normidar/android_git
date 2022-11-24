import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'android_git_platform_interface.dart';

/// An implementation of [AndroidGitPlatform] that uses method channels.
class MethodChannelAndroidGit extends AndroidGitPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('android_git');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
