<template>
  <div>
    <v-snackbar
      v-model="snackbar.bool"
      color="success"
      absolute
      outlined
      timeout="1500"
      top
    >
      {{ snackbar.content }}
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
