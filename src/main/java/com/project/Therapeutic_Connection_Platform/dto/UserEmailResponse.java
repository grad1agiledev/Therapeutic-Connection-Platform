package com.project.Therapeutic_Connection_Platform.dto;

public class UserEmailResponse {
        private String uid;
        private String fullName;
        private String role;

        public UserEmailResponse(String uid, String fullName, String role) {
            this.uid = uid;
            this.fullName = fullName;
            this.role = role;
        }

        public String getUid() {
            return uid;
        }

        public String getFullName() {
            return fullName;
        }

        public String getRole() {
            return role;
        }
    }


