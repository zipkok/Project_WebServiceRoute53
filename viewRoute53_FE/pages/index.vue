<script>
export default {
  middleware: [
    function ({ redirect }) {
      redirect('/route53')
    },
  ],
}
</script>
