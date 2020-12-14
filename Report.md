# Final Report
Yujin Park yp16@zips.uakron.edu

## Comparison
Jenkins and CircleCi have same functionality, but the difference between two is the ease of use. 
Running jenkins may be difficult without experience with docker. In Jenkins, setting build environmnet is little complicated. Jenkins allows build to run only within image which is specified. Jenkins's web UI is slow and unresposive due to use various plug-ins. But, CircleCi is continuously being developed as a popular update. There is faster and more responsive support. Jenkins can support multiple tasks through multi-threading, while CircleCi supports parallelism that can be achieved with project settings. Jenkins configures build using the Jenkins UI with the settings stored on jenkins server. so, it is difficult to exchange setup information with a group. But, CircleCi can configure all actions within the single .yaml file for build. CIrcleCi connets directly to the Github repository and easy to share and backup with group member. But, I need to upload for testing each work. Also, CircleCI has features for debugging via ssh and it makes debugging easier, while jenkins manually debug by clicking action. The Jenkins environment changes only when the user directs it, but CircleCi have matter of changing the environment without notice. Also, we cannot get many information about what's going on inside. So, finding bug is not easy. In jenkins, the team must install all dependencies and it there is a problem, they debug to maintain the jenkins server. In CircleCi, all works in a new environment that all dependencies are installed, you do not need to keep the server. 
In conclusion, Jenkins and CircleCi have similar functionality, to run simple pipline and spend less time, CircleCi is a better than Jenkins. 


## My feature
### Timeout
Jenkns stops running the pipe line when global runtime limit is specified. It allows to define time limits at a 
certain stage. This function prevents jenkins from disrupting operation. 

```
options {
        timeout(time: 1, unit: 'HOURS') 
   }
```

### archiveArtifacts
The archiveArtifacts allows developers to access package through jenkins UI and download later. 
Also, if build is successful, onlyIfSuccessful: true will run. Using post, 
all shell commands are completed and you can access the named build file.

```
   post {
       success{
          archiveArtifacts artifacts: 'build/*.deb', onlyIfSuccessful: true
       }
   }
```
