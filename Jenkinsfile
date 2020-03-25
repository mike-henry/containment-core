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
       NEXUS_REPOSITORY = "local-builds"
       // Jenkins credential id to authenticate to Nexus OSS
       NEXUS_CREDENTIAL_ID = "nexus-credentials"

       PROJECT_NAME = "containment-core"

       SETTINGS_XML = './settings.xml'


    }

    stages {
        stage('Initialise') {
            steps {
                configFileProvider([configFile(fileId: 'e1e9d5d0-3f70-410e-a096-38585ed36d99', variable: 'MAVEN_SETTINGS_FILE')]){
                echo '.Initialising..'

                sh '''
                 echo "PATH = ${PATH}"
                 echo "M2_HOME = ${M2_HOME}"
                 echo "MAVEN_HOME = ${MAVEN_HOME}"
                 echo "JAVA_HOME = ${JAVA_HOME}"
                 echo "MAVEN_SETTINGS_FILE ${MAVEN_SETTINGS_FILE}"
                 cp  ${MAVEN_SETTINGS_FILE} ${SETTINGS_XML}
                 '''


            }}
        }
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'mvn -s ${SETTINGS_XML} clean compile'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh 'mvn -s ${SETTINGS_XML} test'
            }
        }
        stage('Package') {
            steps {
                echo 'Packaging..'
                sh "mvn -s ${SETTINGS_XML} package -DskipTests=true"
            }
        }


        stage('Deploy') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Deploying.. api'
                script {
                    // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                    pom = readMavenPom file:  "${PROJECT_NAME}-api/pom.xml"
                    // Find built artifact under target folder
                    filesByGlob = findFiles(glob: "${PROJECT_NAME}-api/target/*.${pom.packaging}")
                    // Print some info from the artifact found
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    // Extract the path from the File found
                    artifactPath = filesByGlob[0].path
                    // Assign to a boolean response verifying If the artifact name exists
                    artifactExists = fileExists artifactPath
                    if (artifactExists) {

                        echo "*** File:  group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"

                        nexusArtifactUploader(
                                nexusVersion: NEXUS_VERSION,
                                protocol: NEXUS_PROTOCOL,
                                nexusUrl: NEXUS_URL,
                                groupId: pom.groupId,
                                version: pom.version,
                                repository: NEXUS_REPOSITORY,
                                credentialsId: NEXUS_CREDENTIAL_ID,
                                artifacts: [
                                        // Artifact generated such as .jar, .ear and .war files.
                                        [
                                                artifactId: pom.artifactId,
                                                classifier: '',
                                                file      : artifactPath,
                                                type      : pom.packaging],
                                        // Lets upload the pom.xml file for additional information for Transitive dependencies
                                        [
                                                artifactId: pom.artifactId,
                                                classifier: '',
                                                file      : "pom.xml",
                                                type      : "pom"
                                        ]
                                ])
                    }


                }
                echo 'Deploying.. service'
                script {
                      // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                      pom = readMavenPom file:  "${PROJECT_NAME}-service/pom.xml"
                      // Find built artifact under target folder
                      filesByGlob = findFiles(glob: "${PROJECT_NAME}-service/target/*.${pom.packaging}")
                      // Print some info from the artifact found
                      echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                      // Extract the path from the File found
                      artifactPath = filesByGlob[0].path
                      // Assign to a boolean response verifying If the artifact name exists
                      artifactExists = fileExists artifactPath
                      if (artifactExists) {

                          echo "*** File:  group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"

                          nexusArtifactUploader(
                                  nexusVersion: NEXUS_VERSION,
                                  protocol: NEXUS_PROTOCOL,
                                  nexusUrl: NEXUS_URL,
                                  groupId: pom.groupId,
                                  version: pom.version,
                                  repository: NEXUS_REPOSITORY,
                                  credentialsId: NEXUS_CREDENTIAL_ID,
                                  artifacts: [
                                          // Artifact generated such as .jar, .ear and .war files.
                                          [
                                                  artifactId: pom.artifactId,
                                                  classifier: '',
                                                  file      : artifactPath,
                                                  type      : pom.packaging],
                                          // Lets upload the pom.xml file for additional information for Transitive dependencies
                                          [
                                                  artifactId: pom.artifactId,
                                                  classifier: '',
                                                  file      : "pom.xml",
                                                  type      : "pom"
                                          ]
                                  ])
                      }
                }

            }
        }
    }
}