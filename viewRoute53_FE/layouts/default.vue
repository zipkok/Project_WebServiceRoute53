<template>
  <v-app dark>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant="miniVariant"
      :clipped="clipped"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title v-text="item.title" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar :clipped-left="clipped" fixed app>
      <v-app-bar-nav-icon @click="toggleDrawer()" />
      <v-icon @click.stop="miniVariant = !miniVariant"
        >mdi-chevron-left
      </v-icon>

      <v-divider class="mr-2 pl-2" vertical />

      <v-card-title class="d-flex justify-center" v-text="title" />
    </v-app-bar>
    <v-main>
      <v-container>
        <nuxt />
      </v-container>
    </v-main>

    <v-footer :absolute="!fixed" app>
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      clipped: true,
      drawer: false,
      fixed: false,
      items: [
        {
          icon: 'mdi-apps',
          title: 'Admin',
          to: '/route53/admin',
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Route53',
          to: '/route53',
        },
      ],
      miniVariant: false,

      title: 'PSMT',
    }
  },

  methods: {
    toggleDrawer() {
      this.drawer = !this.drawer
    },
  },
}
</script>
