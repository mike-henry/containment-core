pipeline {
    agent any
 tools {

        maven 'maven_3'

    }
    environment {
        // This can be nexus3 or nexus2
        NEXUS_VERSION = "nexus3"
        // This can be http or https
        NEXUS_PROTOCOL = "http"
        // Where your Nexus is running
        NEXUS_URL = "nexus:8081"
        // Repository where we will upload the artifact
        NEXUS_REPOSITORY = "local"
        // Jenkins credential id to authenticate to Nexus OSS
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
    }

    stages {
        stage('Initialise') {
            steps {
                echo '.Initialising..'

                sh '''
                 echo "PATH = ${PATH}"
                 echo "M2_HOME = ${M2_HOME}"
                 echo "MAVEN_HOME = ${MAVEN_HOME}"
                 echo "JAVA_HOME = ${JAVA_HOME}"

                 '''
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'

                sh   'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
          steps {
            script {
            // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
             pom = readMavenPom file: "pom.xml";
             echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
             //sh "mvn package -DskipTests=true"
            }
          }
        }
    }
}