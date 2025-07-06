// src/components/DeleteAnimeConfirm.jsx
import React, { useState } from 'react'
import PropTypes from 'prop-types'

export default function DeleteAnimeConfirm({ onDelete }) {
    const [title, setTitle] = useState('')
    const [msg, setMsg]     = useState(null)

    function handleSubmit(e) {
        e.preventDefault()
        const success = onDelete(title)
        if (success) {
            setMsg(`✅ "${title}" deleted.`)
        } else {
            setMsg(`⚠️ No anime found with title "${title}".`)
        }
        setTitle('')
    }

    return (
        <div className="space-y-4 max-w-md">
            <form onSubmit={handleSubmit} className="space-y-2">
                <div>
                    <label className="block font-medium">Title to Delete</label>
                    <input
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        required
                        className="w-full border rounded p-2"
                        placeholder="Exact title"
                    />
                </div>
                <button
                    type="submit"
                    className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                >
                    Delete Anime
                </button>
            </form>
            {msg && <p className="text-sm">{msg}</p>}
        </div>
    )
}

DeleteAnimeConfirm.propTypes = {
    onDelete: PropTypes.func.isRequired
}
