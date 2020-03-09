pipeline {
    agent any
 tools {

        maven 'maven_3'

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
                echo 'Deploying....'
            }
        }
    }
}