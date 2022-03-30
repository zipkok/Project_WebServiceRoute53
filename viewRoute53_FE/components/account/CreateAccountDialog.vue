<template>
  <div>
    <v-dialog v-model="booleanDialog" persistent max-width="600px">
      <template v-slot:activator="{ on: dialog, attrs: dialogattr }">
        <v-tooltip bottom>
          <template v-slot:activator="{ on: tooltip, attrs: tooltipattr }">
            <v-btn
              color="info"
              dark
              small
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
        <div style="background-color: #fffff0">
          <v-card-title>
            <span class="text-h5"> 신규 생성 </span>
          </v-card-title>
        </div>
        <v-divider></v-divider>
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
                    v-model="HostedzoneName"
                    label="Zone Name*"
                    required
                  />
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="HostedzoneId"
                    label="Zone Id*"
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
          <v-btn color="blue darken-1" nuxt text @click="cancel()">
            취소
          </v-btn>
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
      booleanDialog: false,
      // Form Validate
      valid: false,

      // Form Data
      HostedzoneName: '',
      HostedzoneId: '',
      team: '',
      awsAccount: '',
      awsAccessKey: '',
      awsCredentialKey: '',
    }
  },

  methods: {
    toggleDialog() {
      this.booleanDialog = !this.booleanDialog
    },

    initialContents() {
      this.HostedzoneName = ''
      this.HostedzoneId = ''
      this.team = ''
      this.awsAccount = ''
      this.awsAccessKey = ''
      this.awsCredentialKey = ''
    },

    cancel() {
      try {
        this.initialContents()
      } catch (error) {
      } finally {
        this.toggleDialog()
      }
    },

    async saveAccount(items) {
      // TODO: Validattion 조건 만들어야한다.
      if (this.$refs.form.validate()) {
        await this.$store.dispatch('account/saveAccount', {
          hostedZoneName: this.HostedzoneName,
          hostedZoneId: this.HostedzoneId,
          team: this.team,
          accountName: this.awsAccount,
          awsAccessKey: this.awsAccessKey,
          awsCredentialKey: this.awsCredentialKey,
        })
      }
      await this.$store.dispatch('account/loadAccountItems')
      await this.initialContents()
      await this.toggleDialog()
    },

    async loadAccountByAccountItems(items) {
      await this.$store.dispatch('account/loadAccountByAccountItems')
    },
  },
}
</script>

<style scoped></style>
