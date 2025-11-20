pipeline {
    agent any

    environment {
        IMAGE_NAME = "sanket558/demo-jenkins-app"
        CONTAINER_NAME = "demo-jenkins-app"
        TAG = "${BUILD_NUMBER}"
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }

    stages {

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh """
                    docker pull ${IMAGE_NAME}:latest || true

                    docker build \
                        --cache-from=${IMAGE_NAME}:latest \
                        -t ${IMAGE_NAME}:latest \
                        -t ${IMAGE_NAME}:${TAG} \
                        .
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo "Pushing image to Docker Hub..."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_PASS'
                )]) {
                    sh """
                        echo "$DOCKERHUB_PASS" | docker login -u "$DOCKERHUB_USER" --password-stdin

                        docker push ${IMAGE_NAME}:latest
                        docker push ${IMAGE_NAME}:${TAG}
                    """
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                echo 'Deploying container...'

                sh """
                    # Rollback backup:
                    docker pull ${IMAGE_NAME}:latest || true

                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    docker run -d \
                        -p 9090:9090 \
                        --name ${CONTAINER_NAME} \
                        ${IMAGE_NAME}:latest
                """

                echo "Waiting for service to start..."

                sleep 15
            }
        }

        stage('Health Check') {
            steps {
                echo "Checking service health..."
                script {
                    timeout(time: 30, unit: 'SECONDS') {
                        retry(5) {
                            sh 'curl -f http://localhost:9090/jenkins/greet'
                            sleep 3
                       }
                   }
                }
            }
        }
    }

    post {
        success {
            echo '🚀 Deployment successful and app is healthy!'
        }
        failure {
            echo '❌ Deployment failed — rolling back.'

            sh """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true
                docker run -d -p 9090:9090 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
            """
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}
