// src/ProfileManagement/NavBar.js
import React from 'react';
import { Link , useNavigate} from 'react-router-dom';
import { useAuth } from './ProfileManagement/UserContext';

function NavBar() {
  const { currentUser, userRole, logOut } = useAuth();
  const navigate = useNavigate();

//clearing firebase section and navigating the home
async function handleLogout() {
    await logOut();
    navigate('/');
  }
 return (
    <nav className="app-nav">
      <Link to="/" className="nav-button">Home</Link>
      <Link to="/therapists" className="nav-button">Therapists</Link>
      <Link to="/about" className="nav-button">About Us</Link>

      {/* only when NOT logged in*/}
           {!currentUser && (
             <>
               <Link to="/login"    className="nav-button">Login</Link>
               <Link to="/register" className="nav-button">Register</Link>
             </>
           )}

          {/* logged in links */}
               {currentUser && (
                 <>
                   {/* only non admin users */}
                   {userRole !== 'admin' && (
                     <Link to="/profile" className="nav-button">Profile</Link>
                   )}

                   {/* admin links */}
                   {userRole === 'admin' && (
                     <>
                       <Link to="/admin/verification" className="nav-button">
                         Verification requests
                       </Link>
                       <Link to="/admin/reviews" className="nav-button">
                         Review Management
                       </Link>
                     </>
                   )}

                   {/* log out link */}
                   <button
                     onClick={handleLogout}
                     className="nav-button"
                     style={{
                       background: 'transparent',
                       border:     'none',
                       cursor:     'pointer'
                     }}
                   >
                     Log out
                   </button>
                 </>
               )}
             </nav>
           );
}

export default NavBar;
