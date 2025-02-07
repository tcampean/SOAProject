import { Card, CardContent, Typography, Avatar, Stack } from "@mui/material";
import { Link } from "react-router-dom";

export default ({ user }) => {
    return (
        <Card variant="outlined" sx={{ borderRadius: "15px", boxShadow: 3, padding: 2, maxWidth: 300 }}>
            <CardContent>
                <Stack direction="column" alignItems="center" spacing={2}>
                    <Avatar sx={{ width: 64, height: 64 }} src={user.avatar || "/default-avatar.png"} />
                    <Typography
                        variant="h6"
                        component={Link}
                        to={user.username}
                        sx={{ textDecoration: "none", color: "#d22d41", fontWeight: "bold" }}
                    >
                        {user.username}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        {user.email}
                    </Typography>
                </Stack>
            </CardContent>
        </Card>
    );
};
