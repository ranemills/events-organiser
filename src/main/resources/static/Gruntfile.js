'use strict';

module.exports = function (grunt) {

  require('load-grunt-tasks')(grunt);
  grunt.loadNpmTasks('grunt-express-server');
  grunt.loadNpmTasks('grunt-contrib-concat');

  grunt.initConfig({
    concat: {
      js: {
        src: [
          'bower_components/jquery/dist/jquery.js',
          'bower_components/tether/dist/js/tether.min.js',
          'bower_components/bootstrap/dist/js/bootstrap.min.js',
          'bower_components/angular/angular.js',
          'bower_components/lodash/dist/lodash.js',
          'bower_components/moment/min/moment.min.js',
          'bower_components/angular-moment/angular-moment.min.js'
        ],
        dest: 'dist/third-party.js'
      },
      css: {
        src: [
          'bower_components/bootstrap/dist/css/bootstrap.css',
          'third-party/font-awesome/css/font-awesome.css'
        ],
        dest: 'dist/third-party.css'
      }
    },
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

  grunt.registerTask('default', ['babel', 'concat:js', 'concat:css']);
  grunt.registerTask('serve', ['default', 'express:mock', 'watch']);

};