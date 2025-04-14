import { initializeApp } from "firebase/app";

import { getAnalytics } from "firebase/analytics";
import { getAuth } from 'firebase/auth';
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyAXMEO5yJjUGzFdojujenKYuRdFRXLmmak",
  authDomain: "therapeutic-connection.firebaseapp.com",
  projectId: "therapeutic-connection",
  storageBucket: "therapeutic-connection.appspot.com",
  messagingSenderId: "114188742137",
  appId: "1:114188742137:web:075c5a44b3c93a1a34e128",
  measurementId: "G-3BQBV5E3GH"
};

const app = initializeApp(firebaseConfig);

const analytics = getAnalytics(app);
const db = getFirestore(app);
const auth = getAuth(app);

export { auth, db };