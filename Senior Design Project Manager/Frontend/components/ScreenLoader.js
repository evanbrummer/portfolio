import React, { createContext, useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import RotateLoader from "react-spinners/RotateLoader.js"
import { createTheme } from '@mui/material/styles';

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

export default function ScreenLoader() {
    return (
        <div className='dead-center'>
            <RotateLoader
                loading={true}
                color={theme.palette.primary.main}
                size={8}
                margin={-15}
            />
        </div>
    )
}