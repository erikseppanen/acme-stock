const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Add support for shadow-cljs output
config.resolver.sourceExts.push('cjs');

// Reduce file watching to avoid EMFILE errors
config.watchFolders = [__dirname];
config.resolver.blockList = [
  /node_modules\/.*\/node_modules/,
  /\.git\/.*/,
  /\.shadow-cljs\/.*/,
];

module.exports = config;
