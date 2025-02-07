import { Box, Button, CssBaseline, FormControl, FormLabel, Stack, styled, TextField, Typography, Paper } from "@mui/material";
import axios from "axios";
import { createCookie } from "../Utility/cookies";
import { useNavigate } from "react-router-dom";

const Container = styled(Box)(({ theme }) => ({
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: theme.palette.background.default,
}));

const AuthBox = styled(Paper)(({ theme }) => ({
    padding: theme.spacing(4),
    borderRadius: "20px",
    boxShadow: theme.shadows[5],
    width: "100%",
    maxWidth: 400,
}));

const StyledButton = styled(Button)(({ theme }) => ({
    backgroundColor: "#d22d41",
    color: "#fff",
    borderRadius: "30px",
    padding: theme.spacing(1.5),
    fontSize: "1rem",
    textTransform: "none",
    '&:hover': {
        backgroundColor: theme.palette.primary.main,
    },
}));

const StyledTypography = styled(Typography)(({ theme }) => ({
    fontFamily: "'Poppins', sans-serif",
    fontWeight: 600,
    color: theme.palette.text.primary,
}));

function Login() {
    const navigate = useNavigate();

    const handleRegister = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        axios.post(`http://host.docker.internal:4000/authentication/register`, {
            email: data.get("email"),
            username: data.get("username"),
            password: data.get("password"),
        }).then(r => {
            createCookie("JWT_TOKEN", r.data.jwt);
            window.location.reload();
        }).catch(() => {
            alert("Invalid account");
        });
    };

    const handleLogin = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        axios.post(`http://host.docker.internal:4000/authentication/authenticate`, {
            email: data.get("email"),
            password: data.get("password")
        }).then(r => {
            createCookie("JWT_TOKEN", r.data.jwt);
            navigate("/users");
        }).catch(() => {
            alert("Invalid account");
        });
    };

    return (
        <Container>
            <CssBaseline />
            <Stack direction={{ xs: "column", md: "row" }} spacing={4}>
                <AuthBox>
                    <StyledTypography variant="h5" align="center" gutterBottom>
                        Sign In
                    </StyledTypography>
                    <Box component="form" onSubmit={handleLogin} noValidate>
                        <FormControl fullWidth margin="normal">
                            <FormLabel>Email</FormLabel>
                            <TextField type="email" name="email" required fullWidth variant="outlined" />
                        </FormControl>
                        <FormControl fullWidth margin="normal">
                            <FormLabel>Password</FormLabel>
                            <TextField type="password" name="password" required fullWidth variant="outlined" />
                        </FormControl>
                        <StyledButton type="submit" fullWidth variant="contained" sx={{ mt: 2 }}>
                            Sign In
                        </StyledButton>
                    </Box>
                </AuthBox>

                <AuthBox>
                    <StyledTypography variant="h5" align="center" gutterBottom>
                        Register
                    </StyledTypography>
                    <Box component="form" onSubmit={handleRegister} noValidate>
                        <FormControl fullWidth margin="normal">
                            <FormLabel>Email</FormLabel>
                            <TextField type="email" name="email" required fullWidth variant="outlined" />
                        </FormControl>
                        <FormControl fullWidth margin="normal">
                            <FormLabel>Username</FormLabel>
                            <TextField type="text" name="username" required fullWidth variant="outlined" />
                        </FormControl>
                        <FormControl fullWidth margin="normal">
                            <FormLabel>Password</FormLabel>
                            <TextField type="password" name="password" required fullWidth variant="outlined" />
                        </FormControl>
                        <StyledButton type="submit" fullWidth variant="contained" sx={{ mt: 2 }}>
                            Register
                        </StyledButton>
                    </Box>
                </AuthBox>
            </Stack>
        </Container>
    );
}

export default Login;
