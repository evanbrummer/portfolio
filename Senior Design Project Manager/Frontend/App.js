import './css/App.css';
import React, { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';

import Login from './user pages/Login';
import Account from './user pages/Account';
import Dashboard from './user pages/Dashboard';
import ClientDash from './user pages/client/ClientDash';
import GroupInfo from './user pages/GroupInfo'
import ProjectInfo from './user pages/ProjectInfo';
import Proposal from './user pages/client/Proposal';
import StudentDash from './user pages/student/StudentDash';
import Preferences from './user pages/student/Preferences';
import ProjectList from './user pages/student/ProjectList';
import Auctions from './user pages/student/Auctions';
import InstructorDash from './user pages/instructor/InstructorDash';
import ProposalsList from './user pages/instructor/ProposalsList';
import ProposalInfo from './user pages/instructor/ProposalInfo';
//import ProposalReview from './user pages/instructor/ProposalReview';
import StudentsList from './user pages/instructor/StudentsList';
import MatchingResults from './user pages/instructor/MatchingResults';
import BoardDash from './user pages/board member/BoardDash';
import ReviewSignUp from './user pages/board member/ReviewSignUp';
import AdvisorDash from './user pages/faculty advisor/AdvisorDash';
import ScreenLoader from './components/ScreenLoader';
import Logout from './user pages/Logout';
import { Snackbar, Alert } from '@mui/material';
import Communications from './user pages/instructor/Communications.jsx';
import MatchingDashboard from './user pages/instructor/MatchingDashboard.jsx';
import { UserProvider, UserContext } from './UserContext'

// App.js

const userType = {
  name: "Instructor"
}

// Student
// Client
// Advisor
// Board
// Instructor

function App() {

  const [snackbarOpen, setSnackbarOpen] = useState(false);

  const handleOpen = () => {
    setSnackbarOpen(true);
  };

  const handleClose = () => {
    setSnackbarOpen(false);
  };

  function noPermission(userTypeName) {
    let userTypeText;
    switch (userTypeName) {
      case 'Student':
        userTypeText = "a Student";
        break;
      case 'Client':
        userTypeText = "a Client";
        break;
      case 'Advisor':
        userTypeText = "an Advisor";
        break;
      case 'Board':
        userTypeText = "a Board Member";
        break;
      case 'Instructor':
        userTypeText = "an Instructor";
        break;
      default:
        break;
    }
    return (
      <Alert severity="error"><strong>No permission:</strong> You must be {userTypeText} to access this page</Alert>
    )
  }

  const [appLevelUser, setAppLevelUser] = useState(null);

  useEffect(() => {
    setAppLevelUser(JSON.parse(sessionStorage.getItem("USER")));

    setTimeout(() => setAppLevelUser(JSON.parse(sessionStorage.getItem("USER"))), 2000);
  }, []);

  // in the future, don't use this
  async function userHasType(typeName) {

    if (await appLevelUser?.userType?.includes(typeName)) {
      return true;
    }

    return false;
  }

  return (
    <UserProvider>
      {snackbarOpen && (
          <Snackbar anchorOrigin={{ vertical: 'top', horizontal: 'center' }} open={snackbarOpen} onClose={handleClose}>
            {noPermission(appLevelUser?.userType)}
          </Snackbar>
      )}
      <Routes>
        {/* generic pages */}
        <Route path="/" element={<Login />} />
        <Route path="/group-info" element={<GroupInfo userType={appLevelUser?.userType} />} />
        <Route path="/project-info" element={<ProjectInfo userType={appLevelUser?.userType} />} />
        <Route path="/account" element={<Account userType={appLevelUser?.userType} />} />

        {/* client pages */}
        <Route path="/proposal" element={<Proposal userType={appLevelUser?.userType} />} />

        {/* student pages */}
        <Route path="/preferences" element={ userHasType("Student") ? <Preferences userType={appLevelUser?.userType} /> : noPermission('Student')} /> 
        <Route path="/auctions" element={ userHasType("Student") ? <Auctions userType={appLevelUser?.userType} /> : noPermission('Student')} /> 
        <Route path="/approved-projects-list" element={userHasType("Student") ? <ProjectList userType={appLevelUser?.userType} /> : noPermission('Student')} />

        {/* instructor pages */}
        <Route path="/proposals-list" element={<ProposalsList userType={appLevelUser?.userType} />} />
        <Route path="/proposal-info" element={<ProposalInfo userType={appLevelUser?.userType} />} />  
        {/* <Route path="/proposal-review" element={<ProposalReview userType={appLevelUser?.userType} />} />  */}
        <Route path="/students-list" element={<StudentsList userType={appLevelUser?.userType} />} /> 
        {/* <Route path="/project-matching" element={<ProjectMatching userType={appLevelUser?.userType} />} />  */}
        <Route path="/matching-results" element={<MatchingResults userType={appLevelUser?.userType} />} /> 

        {/* board member pages */}
        <Route path="/review-sign-up" element={<ReviewSignUp userType={appLevelUser?.userType} />} />

        {/* faculty advisor pages  */}

        <Route path="/dashboard" element={<Dashboard />} />

        <Route path="/logout" element={<Logout />} />


      </Routes>
    </UserProvider>
  )
}

export default App;
