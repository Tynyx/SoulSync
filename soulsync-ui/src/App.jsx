// src/App.jsx
import {motion, AnimatePresence} from "framer-motion";



import React, { useState } from 'react'
import Nav from './Nav'
import AnimeList       from './components/AnimeList'
import AddAnimeForm    from './components/AddAnimeForm'
import UpdateAnimeForm from './components/UpdateAnimeForm'
import DeleteAnimeConfirm from './components/DeleteAnimeConfirm'
import FileUploader    from './components/FileUploader'
import Recommendations from './components/Recommendations'

import {
    loadAnimeFile,
    addAnime,
    updateAnime,
    deleteAnime,
    recommendAnime
} from './utils/watchListManager'

export default function App() {

    const [view, setView]         = useState('list')
    const [animeList, setAnimeList]   = useState([])
    const [watchedList, setWatchedList] = useState([])

    // 1) load from file
    function handleFileLoad(text) {
        const loaded = loadAnimeFile(text)
        setAnimeList(loaded)
        setView('list')
    }

    // 2) add

    function handleAdd(record) {
        const updated = addAnime(animeList, record);
        setAnimeList(updated);
        setView('list');
    }


    // 3) update
    function handleUpdate(title, patch) {
        if (updateAnime(animeList, title, patch)) {
            // this line is crucial
            setAnimeList([ ...animeList ]);
        }
        setView('list');
    }

    // 4) delete
    const handleDelete = title => {
        const success = deleteAnime(animeList, title)
        setAnimeList([...animeList])   // force re-render
        setView('list')                // go back to list view
        return success
    }

    // 5) recommend
    function handleRecommend() {
        const recs = recommendAnime(animeList, watchedList, 3)
        setWatchedList(recs)
        setView('recommend')
    }

    return (
        <div className="min-h-screen flex items-center justify-center p-6">
            <div className="
        w-full max-w-6xl
        bg-black bg-opacity-40 backdrop-blur-md
        rounded-2xl shadow-2xl
        ring-1 ring-blue-400/50
        overflow-hidden
      ">
            <Nav current={view} onChange={setView} />

            <main className="p-6 ">
                <AnimatePresence exitBeforeEnter>
                    <motion.div
                        key={view}
                        initial={{ opacity: 0, x: 20 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: -20 }}
                        transition={{ duration: 0.3 }}
                        className="absolute inset-0"
                    >
                        {view === 'list' && <AnimeList list={animeList} />}
                        {view === 'add'  && <AddAnimeForm onAdd={handleAdd} />}
                        {view === 'update' && <UpdateAnimeForm onUpdate={handleUpdate} />}
                        {view === 'delete' && <DeleteAnimeConfirm onDelete={handleDelete} />}
                        {view === 'upload' && <FileUploader onLoad={handleFileLoad} />}
                        {view === 'recommend' &&
                            <Recommendations
                                all={animeList}
                                watched={watchedList}
                                onRecommend={handleRecommend}
                            />
                        }
                        {view === 'logout' &&
                            <motion.p
                                initial={{ scale: 0.8 }}
                                animate={{ scale: 1 }}
                                className="text-2xl text-center mt-10"
                            >👋 Goodbye!</motion.p>
                        }
                    </motion.div>
                </AnimatePresence>
            </main>
           </div>
     </div>
    )
}
