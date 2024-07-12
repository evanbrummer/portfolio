import React, { useEffect, useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom';
import '../css/index.css'
import { TextField, Button, ThemeProvider, FormControlLabel, Checkbox } from '@mui/material';
import HelpButton from '../components/HelpButton.js';
import RotateLoader from "react-spinners/RotateLoader.js"
import { createTheme } from '@mui/material/styles';
import validator from 'validator';
import { UserContext } from '../UserContext.js';

const theme = createTheme({
  palette: {
    primary: {
      main: "#A71930",
    },
    secondary: {
      main: "#888888"
    }
  },
  typography: {
    fontFamily: 'Inter',
  },
});

export default function LoginPage() {

  const EMAIL_STEP = 1;
  const PASSWORD_STEP = 2;

  const [currentStep, setCurrentStep] = useState(EMAIL_STEP);
  const [rememberMe, setRememberMe] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [emailInput, setEmail] = useState('');
  const [passwordInput, setPassword] = useState('');

  const navigate = useNavigate();
  const { user, setUser, type, setType } = useContext(UserContext);

  useEffect(() => {
    if (user != null) {
      console.log("Hi, " + user.firstName);
      sessionStorage.setItem("USER", JSON.stringify(user));
      navigate("/dashboard");
    }
  }, [user]);

  const handleNextClick = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // EMAIL SCREEN
      if (currentStep == EMAIL_STEP) {
        if (!validator.isEmail(emailInput)) {
          throw new Error("Please enter a valid email format.");
        }
        
        let data = {
          email: emailInput,
          password: null
        };

        const response = await fetch("/user/login", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });

        switch (response.status) {
          case 302: // FOUND, response contains user info
          // set user
          let responseUser = await response.json()
          setUser(responseUser);
          setType(responseUser.userType[0]);
          break;
          case 401: // UNAUTHORIZED, need to provide password
            setCurrentStep(PASSWORD_STEP);
          break;
          case 404: // NOT FOUND, user does not exist
            throw new Error("Email not recognized");
          case 500:
            throw new Error("Internal server error, server may be offline");
          default:
            throw new Error(`Something went wrong (${response.status})`);
        }

        setLoading(false);
      }

      // PASSWORD SCREEN
      else if (currentStep == PASSWORD_STEP) {
        if (passwordInput.length == 0) {
          throw new Error("Please enter a valid password.");
        }

        let data = {
          email: emailInput,
          password: passwordInput
        };

        const response = await fetch("/user/login", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });

        window.location.href = '/dashboard';
      }
    } catch (e) {
      setError(e.message);
      setLoading(false);
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <div className="login-page">
        <div className="login-card">

          {currentStep == EMAIL_STEP && (
            <>
              <p className="sign-in-header">Sign in</p>
              <div style={{ width: "60%" }}>
                <form onSubmit={handleNextClick}>
                  <TextField
                    fullWidth
                    disabled={loading}
                    onChange={(event) => setEmail(event.target.value)}
                    id="outlined-email-input" label="Enter your email" type="email" autoComplete="current-password"
                  />
                </form>
              </div>
            </>
          )}
          {currentStep == PASSWORD_STEP && (
            <>
              <p className="sign-in-header">Welcome</p>
              <div style={{ width: "60%" }}>
                <form onSubmit={handleNextClick}>
                  <TextField
                    fullWidth
                    disabled={loading}
                    onChange={(event) => setPassword(event.target.value)}
                    id="outlined-password-input" label="Enter your password" type="password" autoComplete="current-password"
                  />
                </form>
                <FormControlLabel style={{ marginLeft: "auto" }}
                  control={<Checkbox checked={rememberMe} onChange={() => setRememberMe(!rememberMe)} color="primary" />}
                  label="Remember me"
                />
              </div>
            </>
          )}
          <br />
          <Button
            style={{ fontSize: 20, textTransform: 'none', marginBottom: "4vh" }} variant="contained" color="primary"
            onClick={handleNextClick}
            disabled={loading}
          >
            Next
          </Button>

          {error != null ? (<p className="login-error">{error}</p>) : null}

          <RotateLoader
            loading={loading}
            color={theme.palette.primary.main}
            size={8}
            margin={-15}
          />
        </div>
        <div style={{ textAlign: 'center' }}><HelpButton /></div>
      </div>
    </ThemeProvider>
  )
}
