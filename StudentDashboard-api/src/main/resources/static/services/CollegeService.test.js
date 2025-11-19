import { getCollegesList } from "./CollegeService";
describe("getCollegesList", () => {
    const originalFetch = global.fetch;
    let logSpy;
    let errorSpy;

    beforeEach(() => {
        global.fetch = jest.fn();
        logSpy = jest.spyOn(console, "log").mockImplementation(() => {});
        errorSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    });

    afterEach(() => {
        jest.resetAllMocks();
        global.fetch = originalFetch;
        logSpy.mockRestore();
        errorSpy.mockRestore();
    });

    test("resolves with data when fetch response is ok and logs the data", async () => {
        const fakeData = [{ id: 1, name: "Test College" }];
        global.fetch.mockResolvedValue({
            ok: true,
            json: async () => fakeData,
        });

        const data = await getCollegesList();

        expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges");
        expect(data).toEqual(fakeData);
        expect(logSpy).toHaveBeenCalledWith("Fetched colleges data:", fakeData);
    });

    test("rejects with error when response.ok is false and logs the error", async () => {
        global.fetch.mockResolvedValue({
            ok: false,
        });

        await expect(getCollegesList()).rejects.toThrow("Failed to fetch colleges");
        expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges");
        expect(errorSpy).toHaveBeenCalled();
        // ensure the error logged includes our function context
        const loggedArgs = errorSpy.mock.calls[0];
        expect(loggedArgs[0]).toBe("Error in getCollegesList:");
    });

    test("rejects and logs when fetch throws (network error)", async () => {
        const networkError = new Error("Network failure");
        global.fetch.mockRejectedValue(networkError);

        await expect(getCollegesList()).rejects.toThrow("Network failure");
        expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/colleges");
        expect(errorSpy).toHaveBeenCalled();
        const loggedArgs = errorSpy.mock.calls[0];
        expect(loggedArgs[0]).toBe("Error in getCollegesList:");
        expect(loggedArgs[1]).toBe(networkError);
    });
});

