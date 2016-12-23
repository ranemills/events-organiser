'use strict';

module.exports = function (grunt) {

  require('load-grunt-tasks')(grunt);
  grunt.loadNpmTasks('grunt-express-server');

  grunt.initConfig({
    babel: {
      options: {
        sourceMap: true,
        presets: ['es2015']
      },
      dist: {
        files: {
          'dist/app.js': 'scripts/app.js'
        }
      }
    },
    express: {
      mock: {
        options: {
          script: './mock-server.js'
        }
      }
    },
    watch: {
      server: {
        files: ['mock-server.js'],
        tasks: ['express:mock'],
        options: {
          spawn: false
        }
      },
      scripts: {
        files: ['scripts/app.js'],
        tasks: ['babel'],
        options: {
          livereload: true
        }
      },
      html: {
        files: ['html/**/*.html', 'index.html'],
        options: {
          livereload: true
        }
      }
    }
  });

  grunt.registerTask('default', ['babel']);
  grunt.registerTask('serve', ['babel', 'express:mock', 'watch']);

};