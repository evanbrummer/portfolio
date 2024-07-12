import React, { useEffect, useContext } from 'react';
import { UserContext } from '../UserContext.js';
import NavBar from '../components/NavBar.js'
import HelpButton from '../components/HelpButton.js';
import { Link } from 'react-router-dom';
import { createTheme } from '@mui/material/styles';
import { Button, TextField, ThemeProvider, TableCell, TableHead, TableBody, TableRow, TableContainer, Paper, Table, ButtonGroup } from '@mui/material';

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

export default function Account({userType}) {

    const {user} = useContext(UserContext);

    return (
        <ThemeProvider theme={theme}>
            <NavBar/>
            <div className='table-container'>
                <div className='card'>
                    <div style={{width: "90%", marginLeft: "5%", marginTop: '3%', textAlign: 'left'}}>
                        <p className='table-label-weight-500'>Account Info</p> 

                        {renderSingleLine("Email", `${user.email}`, true)}
                        {renderDoubleLine("First Name", `${user.firstName}`, "Last Name", `${user.lastName}`, false)}

                        <br /><br />

                        {renderSingleLine("Other stuff that SSO doesn't provide like major", "", false)}

                        
                        <br />
                    </div>
                </div>
            </div>
            
        </ThemeProvider>
    )
}

function renderDoubleLine(label_1, value_1, label_2, value_2, read_only) {
    return (
        <div>
            <div style={{width:'49%', display: 'inline-block', textAlign: 'left', marginLeft: '0%'}}>
                <TextField
                    fullWidth size='small' id="outlined-password-input" margin="normal" InputProps={{readOnly: read_only,}}
                    label={label_1} defaultValue={value_1}
                />
            </div>
            <div style={{width:'49%', display: 'inline-block', marginLeft: '2%'}}>
                <TextField
                    fullWidth size='small' id="outlined-password-input" margin="normal" InputProps={{readOnly: read_only,}}
                    label={label_2} defaultValue={value_2}
                />
            </div>
        </div>
    )
}

function renderSingleLine(label, value, read_only) {
    return (
        <div>
            <TextField
                multiline fullWidth size='small' id="outlined-password-input" margin="normal" InputProps={{readOnly: read_only,}}
                label={label} defaultValue={value}
            />
            <br></br>
        </div>
    )
}