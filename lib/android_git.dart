
import 'android_git_platform_interface.dart';

class AndroidGit {
  Future<String?> getPlatformVersion() {
    return AndroidGitPlatform.instance.getPlatformVersion();
  }
}
