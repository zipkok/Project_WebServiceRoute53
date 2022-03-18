<template>
  <div>
    <v-dialog v-model="booleanDialog" max-width="600px">
      <template v-slot:activator="{ on: dialog, attrs: dialogattr }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on: tooltip, attrs: tooltipattr }">
            <v-btn
              color="primary"
              dark
              v-bind="{ ...dialogattr, tooltipattr }"
              v-on="{ ...tooltip, ...dialog }"
              @click="toggleDialog"
            >
              <v-icon>mdi-account-plus</v-icon>
            </v-btn>
          </template>

          <span>DNS Zone 등록</span>
        </v-tooltip>
      </template>

      <!-- Form Data -->
      <v-card>
        <v-card-title>
          <span class="text-h5">계정 등록 페이지</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-form
              ref="form"
              v-model="valid"
              @submit.prevent="addTodoSubmitForm"
            >
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    v-model="HostzoneName"
                    label="HostedZone*"
                    required
                  />
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="HostzoneId"
                    label="hostedZoneId*"
                    required
                  />
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="awsAccessKey"
                    label="awsAccessKey*"
                    required
                  />
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="awsCredentialKey"
                    label="awsCredentialKey*"
                    required
                  />
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="team" label="당신의 팀은?*" required />
                </v-col>
                <v-col cols="6">
                  <v-text-field
                    v-model="awsAccount"
                    label="당신의 AWS 계정 이름은?*"
                    required
                  />
                </v-col>
              </v-row>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="cancel()"> 취소 </v-btn>
          <v-btn
            color="blue darken-1"
            text
            type="submit"
            @click="saveAccount()"
          >
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // Form Validate
      valid: false,

      // Form Data
      HostzoneName: '',
      HostzoneId: '',
      team: '',
      awsAccount: '',
      awsAccessKey: '',
      awsCredentialKey: '',
    }
  },
  computed: {
    booleanDialog() {
      return this.$store.state.account.booleanDialog
    },
  },

  methods: {
    toggleDialog() {
      this.$store.commit('account/toggleDialog')
    },

    initialContents() {
      this.HostzoneName = ''
      this.HostzoneId = ''
      this.team = ''
      this.awsAccount = ''
      this.awsAccessKey = ''
      this.awsCredentialKey = ''
    },

    cancel() {
      this.initialContents()
      this.toggleDialog()
    },

    async saveAccount(items) {
      // TODO: Validattion 조건 만들어야한다.
      if (this.$refs.form.validate()) {
        await this.$store
          .dispatch('account/saveAccount', {
            hostedZoneName: this.HostzoneName,
            hostedZoneId: this.HostzoneId,
            team: this.team,
            accountName: this.awsAccount,
            awsAccessKey: this.awsAccessKey,
            awsCredentialKey: this.awsCredentialKey,
          })
          .then((res) => {
            this.$store.dispatch('account/loadAccountItems')
          })
          .catch(() => {
            // TODO: Exception PAGE or Alert 만들기!
          })
      }

      await this.initialContents()
      await this.toggleDialog()
    },
  },
}
</script>

<style scoped></style>
