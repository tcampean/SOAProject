import { AppBar, Box, Container, Toolbar, Typography, Button } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { deleteCookie } from "../Utility/cookies";

function Header() {
    const navigate = useNavigate();

    const handleLogOut = () => {
        deleteCookie("JWT_TOKEN");
        navigate("/");
    };

    return (
        <AppBar position="static" sx={{ backgroundColor: "#d22d41", padding: 1 }}>
            <Container maxWidth="xl">
                <Toolbar sx={{ display: "flex", justifyContent: "space-between", alignItems: "left" }}>
                    <Typography
                        variant="h2"
                        sx={{ fontWeight: "bold", letterSpacing: 1, color: "white" }}
                    >
                        BloggerSpot
                    </Typography>

                    <Box sx={{ display: "flex", gap: 3 }}>
                        <Button component={Link} to="/users" sx={{ color: "white", fontSize: "1.6rem" }}>Home</Button>
                    </Box>

                    <Button onClick={handleLogOut} variant="outlined" sx={{ color: "white", borderColor: "white" }}>
                        Log Out
                    </Button>
                </Toolbar>
            </Container>
        </AppBar>
    );
}

export default Header;
