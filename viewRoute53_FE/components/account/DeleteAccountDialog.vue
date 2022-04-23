<template>
  <div>
    <v-dialog
      v-model="booleanDialog"
      :retain-focus="false"
      persistent
      max-width="600px"
    >
      <template v-slot:activator="{ on: dialog, attrs: dialogattr }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on: tooltip, attrs: tooltipattr }">
            <v-btn
              dark
              x-small
              text
              :retain-focus="false"
              v-bind="{ ...dialogattr, tooltipattr }"
              v-on="{ ...tooltip, ...dialog }"
              @click="enableDialog(item)"
            >
              <v-icon dark>mdi-delete</v-icon>
            </v-btn>
          </template>

          <span>Account 정보 삭제</span>
        </v-tooltip>
      </template>

      <!-- Form Data -->
      <v-card>
        <div style="background-color: #fffff0">
          <v-card-title>
            <span class="text-h5"> Account 정보 수정 </span>
          </v-card-title>
        </div>
        <v-divider></v-divider>
        <v-card-text>
          <v-container>
            <v-form
              ref="form"
              v-model="valid"
              @submit.prevent="deleteAccount(item)"
            >
              <pre id="json">{{ item }}</pre>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" nuxt text @click="cancel()">
                  취소
                </v-btn>
                <v-btn color="blue darken-1" text type="submit"> 삭제 </v-btn>
              </v-card-actions>
            </v-form>
          </v-container>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      booleanDialog: false,

      // Form Validate
      valid: false,
    }
  },

  methods: {
    toggleDialog() {
      this.booleanDialog = !this.booleanDialog
    },

    enableDialog(item) {
      try {
        this.content.push(this.item)
      } catch (error) {
      } finally {
        this.toggleDialog()
      }
    },

    cancel() {
      this.toggleDialog()
    },

    async deleteAccount(value) {
      console.log('deleteAccount' + value)
      if (this.$refs.form.validate()) {
        await this.$store.dispatch('account/deleteAccount', value)
        await this.$store.dispatch('record/deleteRecordSets', {
          HostedZoneId: value.hostedZoneId,
        })
      }
      await this.$store.dispatch('account/loadAccountItems')
      await this.toggleDialog()
    },

    async loadAccountByAccountItems(items) {
      await this.$store.dispatch('account/loadAccountByAccountItems')
    },
  },
}
</script>

<style scoped></style>
