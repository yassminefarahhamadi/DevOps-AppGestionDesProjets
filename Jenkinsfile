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

        stage('Smoke Test') {
            steps {
                sh '''
                    for i in $(seq 1 30); do

                        if docker compose ps --status running | grep -q backend; then
                            echo "Backend is running"
                            break
                        fi

                        echo "Waiting for backend..."
                        sleep 5
                    done

                    if ! docker compose ps --status running | grep -q backend; then
                        echo "Backend failed to start"
                        docker compose logs backend
                        exit 1
                    fi

                    if curl -f http://localhost:4200/ >/dev/null 2>&1; then
                        echo "Frontend is running"
                    else
                        echo "Frontend test failed"
                        docker compose logs frontend
                        exit 1
                    fi

                    echo "Application OK"
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
