// Various game data
const BLOCK_SIZE = 25;
let width        = window.innerWidth;
let height       = window.innerHeight;
const FONT_SIZE  = 24;
const FPS        = 60;

// Game colors
const SNAKE_HEAD_COLOR = { R: 138, G: 162, B: 158 };
let SNAKE_BODY_COLOR = { R: 104, G: 105, B: 99 };
const FOOD_COLOR       = { R: 27,  G: 45,  B: 42 };
const SPECIAL_FOOD_COLOR = { R: 141,  G: 219,  B: 224 };
const GAME_BACKGROUND_COLOR = { R: 61, G: 84, B: 103 };
const GAME_AREA_COLOR = { R: 101, G: 73, B: 86 };
const TEXT_COLOR = 255;

// Directions
const UP    = 0;
const RIGHT = 1;
const DOWN  = 2;
const LEFT  = 3;
const NEW_GAME_BTN = 80; // p

// Server connection
const PORT    = 3000;
const SERVER  = "";
