<template>
  <div>
    <v-snackbar v-model="snackbar.bool" timeout="5000" top>
      {{ snackbar.content }}

      <template v-slot:action="{ attrs }">
        <v-btn color="pink" text v-bind="attrs" @click="snackbar = false">
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
export default {
  props: {
    snackbar: {
      type: Object,
      required: true,
    },
  },
}
</script>

<style scoped></style>
