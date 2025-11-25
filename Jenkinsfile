pipeline {
    agent any

    environment {
        // SonarCloud token stored securely in Jenkins credentials
        SONAR_TOKEN = credentials('sonarcloud-token')
    }

    stages {
        stage('Checkout') {
            steps {
                // Get code from your Git repo
                checkout scm
            }
        }

        // ---------- BACKEND: Build & Test ----------
        stage('Backend - Build & Test') {
            steps {
                dir('StudentDashboard-api') {
                    bat 'mvn clean verify'
                }
            }
        }

        // ---------- BACKEND: SonarCloud ----------
        stage('Backend - SonarCloud') {
            steps {
                dir('StudentDashboard-api') {
                    bat """
mvn sonar:sonar ^
  -Dsonar.projectKey=santhosh1277_AgileProjectTeamMavericks ^
  -Dsonar.organization=santhosh1277 ^
  -Dsonar.host.url=https://sonarcloud.io ^
  -Dsonar.login=%SONAR_TOKEN%
"""
                }
            }
        }

        // ---------- FRONTEND: Install ----------
        stage('Frontend - Install') {
            steps {
                dir('student-dashboard-ui') {
                    bat 'npm ci'
                    // If you later add tests: bat 'npm test -- --watch=false'
                }
            }
        }

        // ---------- FRONTEND: SonarCloud ----------
        stage('Frontend - SonarCloud') {
            steps {
                dir('student-dashboard-ui') {
                    bat """
npx sonar-scanner ^
  -Dsonar.projectKey=santhosh1277_AgileProjectTeamMavericks ^
  -Dsonar.organization=santhosh1277 ^
  -Dsonar.sources=src ^
  -Dsonar.exclusions=**/node_modules/**,build/** ^
  -Dsonar.host.url=https://sonarcloud.io ^
  -Dsonar.login=%SONAR_TOKEN%
"""
                }
            }
        }
    }
}
