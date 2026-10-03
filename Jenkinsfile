pipeline {
    agent any

    environment {
        DOCKERHUB_USER = 'yassmine2004'
        BACKEND_IMAGE  = "${DOCKERHUB_USER}/projets-backend"
        FRONTEND_IMAGE = "${DOCKERHUB_USER}/projets-frontend"
        TAG            = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Images') {
            steps {
                sh '''
                    docker build \
                        -t $BACKEND_IMAGE:$TAG \
                        -t $BACKEND_IMAGE:latest \
                        ./backend

                    docker build \
                        -t $FRONTEND_IMAGE:$TAG \
                        -t $FRONTEND_IMAGE:latest \
                        ./frontend
                '''
            }
        }

        stage('Push Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DH_PASS" | docker login \
                            -u "$DH_USER" \
                            --password-stdin

                        docker push $BACKEND_IMAGE:$TAG
                        docker push $BACKEND_IMAGE:latest

                        docker push $FRONTEND_IMAGE:$TAG
                        docker push $FRONTEND_IMAGE:latest
                    '''
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    docker compose down || true
                    docker compose pull
                    docker compose up -d
                    docker compose ps
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}
