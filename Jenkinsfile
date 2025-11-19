pipeline {
    agent any

    environment {
        IMAGE_NAME = "sanket558/demo-jenkins-app"
        CONTAINER_NAME = "demo-jenkins-app"
    }

    stages {

        stage('Build Maven Project') {
            agent{
                docker{
                    image 'maven:3.9.6-eclipse-temurin-21'
                    args '-v $WORKSPACE:/app -w /app'
                }
            }
            steps {
                echo 'Building Maven project using Dockerized Maven...'
                sh """
                    mvn clean package -DskipTests
                """
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-21'
                    args "-v $WORKSPACE:/app -w /app"
                }
            }
            steps {
                echo 'Running unit tests...'
                sh """
                    mvn test
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh """
                    docker build -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Logging in and pushing image to Docker Hub...'

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_PASS'
                )]) {

                    sh """
                        echo "$DOCKERHUB_PASS" | docker login -u "$DOCKERHUB_USER" --password-stdin
                        docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying container...'

                sh """
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true
                    docker run -d -p 9090:9090 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                """
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Project deployed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
