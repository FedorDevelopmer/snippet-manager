// components/AppInitGuard.tsx
import { useEffect, useState } from 'react';
import { tagsAPI } from '../api/global-api';
import OfflineViewComponent from './views/OfflineView';
import MainMenu from './MainMenu';
import { useNavigate } from 'react-router';

const AppLaunchGuard = () => {
    const [isReady, setIsReady] = useState(false);
    const [hasError, setHasError] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        const checkConnection = async () => {
            try {
                await tagsAPI.get('');
                setIsReady(true);
            } catch (error: any) {
                if (!error.response) {
                    setHasError(true);
                } else {
                    setIsReady(true);
                }
            }
        };

        checkConnection();
    }, []);

    if (hasError) {
        navigate("/offline");
    }

    if (isReady) {
        navigate("/main");
    }
    return <div className="loader">Loading...</div>;
    
};

export default AppLaunchGuard;