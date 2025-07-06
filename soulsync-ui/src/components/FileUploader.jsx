// src/components/FileUploader.jsx
import React, { useState } from 'react'
import PropTypes from 'prop-types'

export default function FileUploader({ onLoad }) {
    const [error, setError]       = useState(null)

    function handleChange(e) {
        setError(null)
        const file = e.target.files?.[0]
        if (!file || !file.name.endsWith('.txt')) {
            setError('Please select a .txt file')
            return
        }

        const reader = new FileReader()
        reader.onload = () => {
            // reader.result should be a string here:
            const text = reader.result
            if (typeof text !== 'string') {
                setError('Failed to read file as text')
                return
            }
            onLoad(text)
        }
        reader.onerror = () => setError('Failed to read file')
        reader.readAsText(file)
    }

    return (
        <div className="space-y-2">
            <label className="block">
                <span className="text-gray-700">Upload your anime list</span>
                <input
                    type="file"
                    accept=".txt"
                    onChange={handleChange}
                    className="mt-1 block w-full"
                />
            </label>
            {error && <p className="text-red-500">{error}</p>}
        </div>
    )
}

FileUploader.propTypes = {
    onLoad: PropTypes.func.isRequired
}
