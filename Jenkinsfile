
PORT=4000

POSTGRES_PASSWORD=eef0d359f7afaf23d90d3a75f11626e61bcb5c2cf505d65de87416cbfb33eed1
POSTGRES_PASSWORD_LOCAL=Nhoxkill500

POSTGRES_USERNAME=gxuumymkvzuzpq
POSTGRES_USERNAME_LOCAL=postgres

POSTGRES_URI=postgresql://ec2-3-214-136-47.compute-1.amazonaws.com:5432/da85j6njcdisla
POSTGRES_URI_LOCAL=postgresql://localhost:5432/serverApiMangaCrawlers


CLIENT_POINT_CHANGE_PASS=http://localhost:3000/auth/changepassword
CLIENT_POINT_VERIFY_ACCOUNT=http://localhost:3000/auth/verification

ORIGIN_CLIENT=http://localhost:3000
ORIGIN_PRODUCTION01=https://mangacrawlers-58f1e.web.app
ORIGIN_PRODUCTION02=https://mangacrawlers-58f1e.firebaseapp.com

HOST_PRODUCTION=https://mangaclawers-server.herokuapp.com/


CLOUDINARY_API_KEY=341627665577959
CLOUDINARY_API_SECRET=n26EJWYnUu9_0iRHAaXK0aiQIIE

GOOGLE_CLIENT_ID=264258136890-avt4brhbqbvin83pare4umd3inv49s2t.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=yQnWqXKo3SqDbxkZk-VCuAMm

YOUTUBE_API_KEY=AIzaSyCvlMIC_CCzzwfC2RrUhwEb21yDtLykn6c

JWT_KEY=thisisauthenticationtokensecretkeyformangaclawerswebsite
EMAIL_USERNAME=mangacrawlers123@gmail.com
EMAIL_PASSWORD=MangaManga123

node {
    def mvnHome
    stage('Preparation') { // for display purposes
        // Get some code from a GitHub repository
        git 'https://github.com/BDMChau/MangaCrawlers-server'
        git branch: 'form-returning---for-jenkins-',

        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
    }
    stage('Build') {
        // Run the maven build
        withEnv(["MVN_HOME=$mvnHome"]) {
            if (isUnix()) {
                sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
            } else {
                bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
            }
        }
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts 'target/*.jar'
    }
}
