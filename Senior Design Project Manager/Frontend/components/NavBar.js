import '../css/index.css'
import React, {useContext} from 'react';
import { Link } from 'react-router-dom';
import { AppBar, Toolbar, IconButton, Typography, Menu, Avatar , Tooltip, MenuItem, ThemeProvider } from '@mui/material';
import { createTheme } from '@mui/material/styles';
import IowaStateUniversityLogo from '../images/IowaStateUniversityLogo.png';
import { UserContext } from '../UserContext.js';
import { ArrowDropDown, Margin } from '@mui/icons-material';
import { Button } from '@mui/material';

const theme = createTheme({
    palette: {
      primary: {
        main: "#A71930",
      },
      secondary: {
        main: "#231F20",
      }
    },
});

function NavBar() {

  const {user, type, setType} = useContext(UserContext);

  const [anchorElUser, setAnchorElUser] = React.useState(null);

  const handleOpenRoleMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseRoleMenu = () => {
    setAnchorElUser(null);
  };

  const getRoleColor = () => {
    const colors = {
      Student: '#C8102E',
      Instructor: '#006BA6',
      Advisor: '#F1BE48',
      Client: '#ACA39A',
      Board: '#A2A569'
    };
    return colors[type];
  }

  return (
    <ThemeProvider theme={theme}>
      <AppBar position="static" color="secondary" style={{ height: '135px', justifyContent: 'center' }}>
        <Toolbar disableGutters style={{ paddingLeft: '65px', paddingRight: '65px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', width: '100%' }}>
            <div style={{ alignItems: 'center' }}>
              <Link to="/dashboard">
                <img
                  src={IowaStateUniversityLogo}
                  alt="Iowa State University Logo"
                  style={{ width: '50%', height: '50%', maxWidth: '480px', flexShrink: 0 }}
                />
              </Link>
              <Typography
                style={{
                  fontFamily: 'Inter',
                  fontSize: 'min(2.5vw, 30px)',
                  fontStyle: 'normal',
                  fontWeight: 100,
                  lineHeight: 'normal'
                }}
              >
                Senior Design Project Matcher: {type}
              </Typography>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', minWidth: '25%' }}>
              <Tooltip title={`${user?.firstName} ${user?.lastName} (${type})`}>
                <Avatar style={{background: getRoleColor()}}>{user?.firstName?.charAt(0)}</Avatar>
              </Tooltip>

              <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                <Link to="/dashboard" className="nav-link">Dashboard</Link>
                <Link to="/account" className="nav-link">Account</Link>
                <Link to="/logout" className="nav-link">Logout</Link>
                {
                  user?.userType.length > 1 && (
                    <Link onClick={handleOpenRoleMenu} className="nav-link">
                      Switch Role <ArrowDropDown style={{ margin: '-5px' }} />
                    </Link>
                  )
                }
                <Menu
                sx={{ mt: '35px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseRoleMenu}
              >
                {user.userType.map((role) => (
                  <Link to="/dashboard" style={{ textDecoration: 'none', color: 'inherit' }}>
                    <MenuItem key={role} onClick={() => setType(role)} 
                    style={{background: role == type ? getRoleColor() : null, color: role == type ? '#FFFFFF' : null}}>
                      <Typography textAlign="center">{role}</Typography>
                    </MenuItem>
                  </Link>
                ))}
              </Menu>
              </div>

              

            </div>
          </div>
        </Toolbar>
      </AppBar>
    </ThemeProvider>
  );
}
export default NavBar;