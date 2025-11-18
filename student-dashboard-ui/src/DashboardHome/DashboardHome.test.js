import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import DashboardHome from './DashboardHome';
import { getCollegesList } from '../api/Service.js/CollegeService';

jest.mock('../api/Service.js/CollegeService');

describe('DashboardHome', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('renders loading state initially', () => {
        getCollegesList.mockResolvedValue([]); // Mock resolved value
        render(<DashboardHome />);
        expect(screen.getByText(/Loading colleges.../i)).toBeInTheDocument();
    });

    test('renders error message on fetch failure', async () => {
        getCollegesList.mockRejectedValue('Fetch error');
        render(<DashboardHome />);
        
        await waitFor(() => expect(screen.getByText(/Fetch error/i)).toBeInTheDocument());
    });
    
    test('renders college list on successful fetch', async () => {
        const colleges = [
            { id: 1, name: 'College A', location: 'Location A', rank: 1 },
            { id: 2, name: 'College B', location: 'Location B', rank: 2 },
        ];
        getCollegesList.mockResolvedValue(colleges); // Mock resolved value
        render(<DashboardHome />);
        
        await waitFor(() => expect(screen.getAllByText(/College A/i).length).toBeGreaterThan(0));
        await waitFor(() => expect(screen.getAllByText(/College B/i).length).toBeGreaterThan(0));
    });
});