import { createClient } from '@supabase/supabase-js/dist/index.mjs'

const SUPABASE_URL = import.meta.env.VITE_SUPABASE_URL
const SUPABASE_ANON_KEY = import.meta.env.VITE_SUPABASE_ANON_KEY
export const IS_CLOUD_MODE = Boolean(SUPABASE_URL && SUPABASE_ANON_KEY)

export const supabase = IS_CLOUD_MODE
  ? createClient(SUPABASE_URL, SUPABASE_ANON_KEY, {
      auth: {
        persistSession: false,
      },
    })
  : null
