import React from "react";
import PropTypes from "prop-types";

/**
 * Nav component: displays navigation buttons for each app section.
 * Props:
 * - current: string key of the active view
 * - onChange: function to call with new view key
 */
export default function Nav({ current, onChange }) {
    const items = [
        { key: "list", label: "List Anime" },
        { key: "add", label: "Add Anime" },
        { key: "update", label: "Update Anime" },
        { key: "delete", label: "Delete Anime" },
        { key: "upload", label: "Upload TXT" },
        { key: "recommend", label: "Recommendations" },
        { key: "logout", label: "Logout" }
    ];

    return (
        <nav className="bg-white shadow-md px-4 py-2 flex space-x-2">
            {items.map(({ key, label }) => (
                <button
                    key={key}
                    onClick={() => onChange(key)}
                    className={`px-3 py-1 rounded-lg focus:outline-none transition-colors ${
                        current === key ? "bg-blue-500 text-white" : "hover:bg-blue-100 text-gray-700"
                    }`}
                >
                    {label}
                </button>
            ))}
        </nav>
    );
}

Nav.propTypes = {
    current: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired
};
