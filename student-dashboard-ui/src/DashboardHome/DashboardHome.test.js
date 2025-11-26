import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import DashboardHome from './DashboardHome';

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock(
    'react-router-dom',
    () => ({
        useNavigate: () => mockNavigate,
    }),
    { virtual: true }
);

// Mock global fetch
global.fetch = jest.fn();

describe('DashboardHome', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('renders loading state initially', () => {
        global.fetch.mockResolvedValue({
            ok: true,
            json: async () => [],
        });
        render(<DashboardHome />);
        expect(screen.getByText(/Loading colleges.../i)).toBeInTheDocument();
    });

    test('renders error message on fetch failure', async () => {
        global.fetch.mockResolvedValue({
            ok: false,
        });
        render(<DashboardHome />);
        
        await waitFor(() => expect(screen.getByText(/Failed to fetch colleges/i)).toBeInTheDocument());
    });
    
    test('renders college list on successful fetch', async () => {
        const colleges = [
            { id: 1, name: 'College A', location: 'Location A', rank: 1 },
            { id: 2, name: 'College B', location: 'Location B', rank: 2 },
        ];
        global.fetch.mockResolvedValue({
            ok: true,
            json: async () => colleges,
        });
        render(<DashboardHome />);
        
        await waitFor(() => expect(screen.getAllByText(/College A/i).length).toBeGreaterThan(0));
        await waitFor(() => expect(screen.getAllByText(/College B/i).length).toBeGreaterThan(0));
    });
});