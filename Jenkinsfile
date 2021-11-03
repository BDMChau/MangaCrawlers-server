node {
    def mvnHome
    stage('Preparation') { // for display purposes
        // Get some code from a GitHub repository
        git url: 'https://github.com/BDMChau/MangaCrawlers-server', branch: 'jenkins-for-testing'

        mvnHome = tool 'M3'
    }
    stage('Build') {
        echo "CurrentResult: $currentBuild.result"
        if(currentBuild.result ==  null){
            try{
                withEnv(["MVN_HOME=$mvnHome"]){
                    if (isUnix()) {
                        sh "mvn -Dmaven.test.failure.ignore=true clean package"
                    } else {
                        bat "mvn -Dmaven.test.failure.ignore=true clean package"
                    }
                }
            } catch(Exception ex){
                currentBuild.result = "UNSTABLE"
            }
        }
    }

    stage('Results') {
        if(currentBuild.result == 'UNSTABLE'){
              echo "CurrentResult failed: $currentBuild.result"
            // if unit tests have failed currentBuild will be 'UNSTABLE'
            emailext attachLog: true, body: currentBuild.result, subject: 'From mangacrawlers-server-spring pipeline', to: 'bdmchau10005@gmail.com, pdm.Triet@gmail.com'
        } else{
            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts 'target/*.jar'
            echo "CurrentResult OK: $currentBuild.result"
            emailext attachLog: true, body:currentBuild.result, subject: 'From mangacrawlers-server-spring pipeline', to: 'bdmchau10005@gmail.com, pdm.Triet@gmail.com'
        }
    }
}